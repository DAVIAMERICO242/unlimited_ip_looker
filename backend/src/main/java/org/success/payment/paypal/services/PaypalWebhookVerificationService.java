package org.success.payment.paypal.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.success.payment.paypal.DTOs.IncomingWebhook;
import org.success.payment.paypal.DTOs.WebhookSignatureRequest;
import org.success.payment.paypal.DTOs.WebhookSignatureResponse;
import org.success.payment.paypal.PaypalContext;
import org.success.payment.paypal.enums.WebhookVerificationStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Map;

@Service
public class PaypalWebhookVerificationService extends PaypalContext {

    public Boolean isVerifiedWebhook(HttpServletRequest request, Object body) {
        String verifyUrl = this.URL + "/v1/notifications/verify-webhook-signature";
        WebhookSignatureRequest payload = new WebhookSignatureRequest();
        String paypalAuthAlgo = request.getHeader("paypal-auth-algo");
        String paypalCertUrl = request.getHeader(("paypal-cert-url"));
        String paypalTransmissionId = request.getHeader(("paypal-transmission-id"));
        String paypalTransmissionSig = request.getHeader(("paypal-transmission-sig"));
        String transmissionTimeString = request.getHeader(("paypal-transmission-time"));
        payload.setAuth_algo(paypalAuthAlgo);
        payload.setCert_url(paypalCertUrl);
        payload.setTransmission_id(paypalTransmissionId);
        payload.setTransmission_sig(paypalTransmissionSig);
        payload.setTransmission_time(transmissionTimeString);
        payload.setWebhook_id(this.webhookID);
        payload.setWebhook_event(body);
        // Create the HTTP entity
        HttpEntity httpEntity = new HttpEntity<>(payload, this.authorizedHeaders);

        // Call PayPal's verification endpoint
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<WebhookSignatureResponse> response = restTemplate.exchange(verifyUrl, HttpMethod.POST, httpEntity, WebhookSignatureResponse.class);
            WebhookVerificationStatus status = response.getBody().getVerification_status();
            return status.equals(WebhookVerificationStatus.SUCCESS);  // Return true if verification succeeded
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if any error occurs
        }
    }
}
