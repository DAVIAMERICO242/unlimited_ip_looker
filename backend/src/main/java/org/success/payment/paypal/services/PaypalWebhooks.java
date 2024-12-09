package org.success.payment.paypal.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.success.payment.paypal.DTOs.IncomingWebhook;
import org.success.payment.paypal.PaypalContext;

@Service
public class PaypalWebhooks extends PaypalContext {

    @Async
    public void processWebhook(IncomingWebhook payload){
        System.out.println(payload);
    }


}
