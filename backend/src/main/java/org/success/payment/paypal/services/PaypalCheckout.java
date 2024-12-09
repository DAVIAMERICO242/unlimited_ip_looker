package org.success.payment.paypal.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.success.payment.CheckoutRedirect;
import org.success.payment.paypal.DTOs.CreateSubscriptionRequest;
import org.success.payment.paypal.DTOs.CreateSubscriptionResponse;
import org.success.payment.paypal.DTOs.WebhookSignatureResponse;
import org.success.payment.paypal.PaypalContext;

import java.util.Base64;
import java.util.Objects;

@Service
public class PaypalCheckout extends PaypalContext {

    public CheckoutRedirect createCheckout(String email){
        CreateSubscriptionRequest payload = new CreateSubscriptionRequest();
        payload.setPlan_id(this.recurringPlanID);
        payload.setCustom_id(email);
        CreateSubscriptionRequest.CallbackURLS urls = new CreateSubscriptionRequest.CallbackURLS();
        urls.setReturn_url(this.frontendUrl + "/success_checkout");
        urls.setCancel_url(this.frontendUrl + "/canceled_checkout");
        payload.setApplication_context(urls);
        HttpEntity httpEntity = new HttpEntity<>(payload,this.authorizedHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CreateSubscriptionResponse> response = restTemplate.exchange(this.URL + "/v1/billing/subscriptions",
                HttpMethod.POST,httpEntity,
                CreateSubscriptionResponse.class);
        String redirectURL = response.getBody().getLinks().get(0).getHref();
        return new CheckoutRedirect(redirectURL);
    }
}
