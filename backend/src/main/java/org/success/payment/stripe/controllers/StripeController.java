package org.success.payment.stripe.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.success.payment.stripe.services.StripeCheckout;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private StripeCheckout stripeCheckout;

    @PostMapping("/create-checkout-session")
    public ResponseEntity createCheckout(){
        try{
            return ResponseEntity.ok().body(stripeCheckout.createCheckoutSession());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
