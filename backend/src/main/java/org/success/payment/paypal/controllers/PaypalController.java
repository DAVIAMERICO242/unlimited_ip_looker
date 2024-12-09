package org.success.payment.paypal.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.success.payment.paypal.DTOs.IncomingWebhook;
import org.success.payment.paypal.services.PaypalWebhookVerificationService;
import org.success.payment.paypal.services.PaypalWebhooks;

@RestController
@RequestMapping("/paypal")
public class PaypalController {

    @Autowired
    private PaypalWebhooks paypalWebhooks;

    @Autowired
    private PaypalWebhookVerificationService paypalWebhookVerificationService;

    @PostMapping("/webhook")
    public ResponseEntity webhook(@RequestBody IncomingWebhook payload, HttpServletRequest request){//se for subscription activated o email que vai chegar aqui é o do checkout
        try{
            System.out.println("payload");
            if(paypalWebhookVerificationService.isVerifiedWebhook(request,payload)){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
