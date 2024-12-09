package org.success.payment.paypal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.success.payment.paypal.DTOs.IncomingWebhook;
import org.success.payment.paypal.services.PaypalWebhooks;

@RestController
@RequestMapping("/paypal")
public class PaypalController {

    @Autowired
    private PaypalWebhooks paypalWebhooks;

    @PostMapping("/webhook")
    public ResponseEntity webhook(@RequestBody IncomingWebhook payload){//se for subscription activated o email que vai chegar aqui é o do checkout
        try{
            System.out.println("payload");
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
