package org.success.payment.paypal.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.success.payment.paypal.DTOs.WebhookSignatureRequest;
import org.success.payment.paypal.DTOs.WebhookSignatureResponse;
import org.success.payment.paypal.PaypalContext;
import org.success.payment.paypal.enums.WebhookVerificationStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Map;

@Service
public class PayPalWebhookVerificationService extends PaypalContext {

    public Boolean isVerifiedWebhook(Map<String, String> headers, Object body) {
        String verifyUrl = this.URL + "/v1/notifications/verify-webhook-signature";
        WebhookSignatureRequest payload = new WebhookSignatureRequest();
        payload.setAuth_algo(headers.get("paypal-auth-algo"));
        payload.setCert_url(headers.get("paypal-cert-url"));
        payload.setTransmission_id(headers.get("paypal-transmission-id"));
        payload.setTransmission_sig(headers.get("paypal-transmission-sig"));
        payload.setTransmission_time(ZonedDateTime.parse(headers.get("paypal-transmission-time")).toLocalDateTime());
        payload.setWebhook_id(this.webhookID);
        payload.setWebhook_event(body);

        // Set up HTTP headers
        HttpHeaders httpHeaders = new HttpHeaders();
        String basicAuth = Base64.getEncoder().encodeToString((this.clientID + ":" + this.secretKey).getBytes());
        httpHeaders.set("Authorization", "Basic " + basicAuth);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity
        HttpEntity request = new HttpEntity<>(payload, httpHeaders);

        // Call PayPal's verification endpoint
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<WebhookSignatureResponse> response = restTemplate.exchange(verifyUrl, HttpMethod.POST, request, WebhookSignatureResponse.class);
            WebhookVerificationStatus status = response.getBody().getVerification_status();
            return status.equals(WebhookVerificationStatus.SUCCESS);  // Return true if verification succeeded
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if any error occurs
        }
    }
}
