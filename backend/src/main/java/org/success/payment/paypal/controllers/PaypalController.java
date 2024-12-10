package org.success.payment.paypal.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.success.payment.paypal.DTOs.IncomingWebhook;
import org.success.payment.paypal.services.PaypalCheckout;
import org.success.payment.paypal.services.PaypalWebhookVerificationService;
import org.success.payment.paypal.services.PaypalWebhooks;

@RestController
@RequestMapping("/paypal")
public class PaypalController {

    @Autowired
    private PaypalWebhooks paypalWebhooks;

    @Autowired
    private PaypalWebhookVerificationService paypalWebhookVerificationService;

    @Autowired
    private PaypalCheckout paypalCheckout;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/webhook")
    public ResponseEntity webhook(@RequestBody Object payload, HttpServletRequest request){//se for subscription activated o email que vai chegar aqui é o do checkout
        try{
            if(paypalWebhookVerificationService.isVerifiedWebhook(request,payload)){
                IncomingWebhook unserializedWebhook = mapper.convertValue(payload,IncomingWebhook.class);
                paypalWebhooks.processWebhook(unserializedWebhook);
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity checkout(@RequestParam(required = false) String webhookCustomerSignature){//se for subscription activated o email que vai chegar aqui é o do checkout
        try{
            return ResponseEntity.ok().body(paypalCheckout.createCheckout(webhookCustomerSignature));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

}
