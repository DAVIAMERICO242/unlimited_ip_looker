package org.success.payment.paypal.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class PayPalWebhookVerificationService {

    @Value("${paypal.api.url}")
    private String paypalApiUrl;

    @Value("${paypal.access.token}")
    private String accessToken;

    @Value("${paypal.webhook.id}")
    private String webhookId;

    public boolean verifyWebhook(Map<String, String> headers, Map<String, Object> body) {
        String verifyUrl = paypalApiUrl + "/v1/notifications/verify-webhook-signature";

        // Prepare the data to verify the signature
        Map<String, Object> payload = Map.of(
                "auth_algo", headers.get("paypal-auth-algo"),
                "cert_url", headers.get("paypal-cert-url"),
                "transmission_id", headers.get("paypal-transmission-id"),
                "transmission_sig", headers.get("paypal-transmission-sig"),
                "transmission_time", headers.get("paypal-transmission-time"),
                "webhook_id", webhookId,
                "webhook_event", body
        );

        // Set up HTTP headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, httpHeaders);

        // Call PayPal's verification endpoint
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(verifyUrl, HttpMethod.POST, request, Map.class);
            String status = (String) response.getBody().get("verification_status");
            return "SUCCESS".equals(status);  // Return true if verification succeeded
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if any error occurs
        }
    }
}
