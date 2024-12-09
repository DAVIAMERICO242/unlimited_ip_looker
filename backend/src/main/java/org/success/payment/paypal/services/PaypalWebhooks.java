package org.success.payment.paypal.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.success.payment.paypal.DTOs.IncomingWebhook;
import org.success.payment.paypal.PaypalContext;

@Service
public class PaypalWebhooks extends PaypalContext {

    @Async
    public void processWebhook(IncomingWebhook payload){
        if(payload.getEvent_type().equals("BILLING.SUBSCRIPTION.ACTIVATED")){//a subscription foi ativa por checkout bem sucedido

        }
        else if(payload.getEvent_type().equals("BILLING.SUBSCRIPTION.CANCELLED")){//a subscription foi cancelada seja por falha sucessiva no pagamento ou o cliente quis

        }
        else if(payload.getEvent_type().equals("BILLING.SUBSCRIPTION.PAYMENT.FAILED")){//pagamento falhou

        }
        else if(payload.getEvent_type().equals("PAYMENT.SALE.COMPLETED")){//cobrança bem sucedida em cima da subscription

        }
    }


}
