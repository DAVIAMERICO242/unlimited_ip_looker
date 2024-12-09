package org.success.payment.paypal.services;

import org.springframework.stereotype.Service;
import org.success.payment.paypal.PaypalContext;

@Service
public class PaypalWebhooks extends PaypalContext {
    public void processWebhook(Object payload){
        System.out.println(payload);
    }


}
