package org.success.payment.paypal.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.success.payment.paypal.DTOs.IncomingWebhook;
import org.success.payment.paypal.PaypalContext;

@Service
public class PaypalWebhooks extends PaypalContext {

    @Autowired
    private SubscriptionPaypalProcessor subscriptionPaypalProcessor;

    @Async
    public void processWebhook(IncomingWebhook payload){
        if(payload.getEvent_type().equals("BILLING.SUBSCRIPTION.ACTIVATED")){//a subscription foi ativa por checkout bem sucedido
            Object subscriptionDataUnserialized = payload.getResource();
            IncomingWebhook.SubscriptionResource subscriptionData = getSerializedSubscriptionResource(subscriptionDataUnserialized);
            this.subscriptionPaypalProcessor.processAfterCheckout(
                    subscriptionData.getCustom_id(),
                    subscriptionData.getSubscriber().getPayer_id(),
                    subscriptionData.getSubscriber().getName().getGiven_name() + " " + subscriptionData.getSubscriber().getName().getSurname()
            );
        }
        else if(payload.getEvent_type().equals("BILLING.SUBSCRIPTION.CANCELLED")){//a subscription foi cancelada seja por falha sucessiva no pagamento ou o cliente quis

        }
        else if(payload.getEvent_type().equals("BILLING.SUBSCRIPTION.PAYMENT.FAILED")){//pagamento falhou

        }
//        else if(payload.getEvent_type().equals("PAYMENT.SALE.COMPLETED")){//cobrança bem sucedida em cima da subscription
//
//        }
    }

    private IncomingWebhook.SubscriptionResource getSerializedSubscriptionResource(Object generic){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(generic,IncomingWebhook.SubscriptionResource.class);
    }


}
