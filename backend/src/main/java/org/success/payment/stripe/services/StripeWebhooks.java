package org.success.payment.stripe.services;

import com.stripe.Stripe;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.success.payment.processor.PaymentProcessor;
import org.success.payment.processor.SubscriptionProcessor;
import org.success.payment.stripe.StripeContext;

import java.util.Optional;

@Service
public class StripeWebhooks extends StripeContext {

    @Autowired
    private SubscriptionProcessor subscriptionProcessor;

    @Async
    public void processWebhook(String rawBody, String stripeSignature){
        try{
            Event event = Webhook.constructEvent(rawBody,stripeSignature,this.webhookSecret);
            event.setApiVersion(Stripe.API_VERSION);//versão la no painel
            StripeObject object = this.getAbstractStripeObject(event);
            if(event.getType().equals("checkout.session.completed")){//assinou o plano
                Session checkout = (Session) object;
                subscriptionProcessor.processStripeAfterCheckout(
                        checkout.getCustomer(),
                        checkout.getCustomerDetails().getName(),
                        checkout.getCustomerDetails().getEmail(),
                        checkout.getCustomerDetails().getPhone()
                );
                System.out.println(checkout);
            }
            if(event.getType().equals("charge.succeeded")){//cobrança realizada com sucesso, implementar key ativa aqui, mas não enviar email
                Charge charge = (Charge) object;
                System.out.println(charge);
            }
            if(event.getType().equals("charge.failed")){//cobrança falhou
                Charge charge = (Charge) object;
                System.out.println(charge);
            }
//            if(event.getType().equals("customer.subscription.paused")){//provavelmente é tirgado 1 mes depois de cancelar
//                Subscription subscription = (Subscription) object;
//                System.out.println(subscription);
//            }
//            if(event.getType().equals("customer.subscription.resumed")){//provavelmente é tirgado 1 mes depois de cancelar
//                Subscription subscription = (Subscription) object;
//                System.out.println(subscription);
//            }
            if(event.getType().equals("customer.subscription.deleted")){//é trigado quando a assinatura é cancelada (SEMPRE NO PERIODO DO FATURAMENTO)
                Subscription subscription = (Subscription) object;
                System.out.println(subscription);
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
