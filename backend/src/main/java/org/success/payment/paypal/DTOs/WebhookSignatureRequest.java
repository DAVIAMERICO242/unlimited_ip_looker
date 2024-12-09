package org.success.payment.paypal.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebhookSignatureRequest {
    private String auth_algo;
    private String cert_url;
    private String transmission_id;
    private String transmission_sig;
    private LocalDateTime transmission_time;
    private String webhook_id;
    private IncomingWebhook webhook_event;
}
