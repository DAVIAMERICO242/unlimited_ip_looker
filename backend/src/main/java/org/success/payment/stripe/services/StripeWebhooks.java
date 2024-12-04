package org.success.payment.stripe.services;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.success.payment.stripe.StripeContext;

import java.util.Optional;

@Service
public class StripeWebhooks extends StripeContext {

    @Async
    public void processWebhook(String rawBody, String stripeSignature){
        try{
            Event event = Webhook.constructEvent(rawBody,stripeSignature,this.webhookSecret);
            event.setApiVersion(Stripe.API_VERSION);//versão la no painel
            StripeObject object = this.getAbstractStripeObject(event);
            if(event.getType().equals("checkout.session.completed")){//cobrança realizada com sucesso
                Session checkout = (Session) object;
                System.out.println(checkout);
            }
            if(event.getType().equals("charge.succeeded")){//cobrança realizada com sucesso
                Charge charge = (Charge) object;
                System.out.println(charge);
            }
        }catch (Exception e){
            System.out.println("ERRO AO PROCESSAR WEBHOOK");
            System.out.println(e.getLocalizedMessage());
        }
    }

    private StripeObject getAbstractStripeObject(Event event){
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        return dataObjectDeserializer.getObject().get();
    }
}
