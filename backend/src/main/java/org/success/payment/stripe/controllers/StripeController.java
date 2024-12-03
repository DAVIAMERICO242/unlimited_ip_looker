package org.success.payment.stripe.controllers;

import com.stripe.model.Event;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.success.payment.stripe.services.StripeCheckout;
import org.success.payment.stripe.services.StripeWebhooks;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private StripeCheckout stripeCheckout;

    @Autowired
    private StripeWebhooks stripeWebhooks;

    @PostMapping("/create-checkout-session")
    public ResponseEntity createCheckout(){
        try{
            return ResponseEntity.ok().body(stripeCheckout.createCheckoutSession());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity webhook(@RequestBody Event event){
        try{
            stripeWebhooks.processWebhook(event);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
