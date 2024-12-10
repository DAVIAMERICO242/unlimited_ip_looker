package org.success.payment.paypal.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.success.payment.CheckoutRedirect;
import org.success.payment.paypal.DTOs.CreateSubscriptionRequest;
import org.success.payment.paypal.DTOs.CreateSubscriptionResponse;
import org.success.payment.paypal.DTOs.WebhookSignatureResponse;
import org.success.payment.paypal.PaypalContext;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Service
public class PaypalCheckout extends PaypalContext {

    public CheckoutRedirect createCheckout(String webhookCustomerSignature){
        CreateSubscriptionRequest payload = new CreateSubscriptionRequest();
        payload.setPlan_id(this.recurringPlanID);
        if(webhookCustomerSignature!=null && !webhookCustomerSignature.isBlank()){
            payload.setCustom_id(webhookCustomerSignature);
        }else{
            payload.setCustom_id(generateWebhookCustomerSignature());
        }
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

    public String generateWebhookCustomerSignature(){
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 14; i++) {
            int index = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length()); // Random index for selecting a character
            password.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(index)); // Append the character to the password
        }
        return password.toString();
    }
}
