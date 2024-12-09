package org.success.payment.paypal.services;

import org.springframework.stereotype.Service;

@Service
public class PaypalWebhooks {
    public void processWebhook(Object payload){

        System.out.println(payload);
    }


}
