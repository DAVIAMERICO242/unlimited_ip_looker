package org.success.payment.stripe.controllers;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.success.payment.stripe.StripeContext;
import org.success.payment.stripe.services.StripeCheckout;
import org.success.payment.stripe.services.StripeCustomerPortal;
import org.success.payment.stripe.services.StripeWebhooks;

@RestController
@RequestMapping("/stripe")
public class StripeController extends StripeContext {

    @Autowired
    private StripeCheckout stripeCheckout;

    @Autowired
    private StripeWebhooks stripeWebhooks;

    @Autowired
    private StripeCustomerPortal stripeCustomerPortal;

    @PostMapping("/create-checkout-session")
    public ResponseEntity createCheckout(){
        try{
            return ResponseEntity.ok().body(stripeCheckout.createCheckoutSession());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity webhook(@RequestBody String eventUnserialized, HttpServletRequest request){
        try{
            String signature = request.getHeader("Stripe-Signature");
            stripeWebhooks.processWebhook(eventUnserialized,signature);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/portal")
    public ResponseEntity createCustomerPortal(@RequestParam String customerId){
        try{
            return ResponseEntity.ok().body(stripeCustomerPortal.createShortLivedPortal(customerId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
