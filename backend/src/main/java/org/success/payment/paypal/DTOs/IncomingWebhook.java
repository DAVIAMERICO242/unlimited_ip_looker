package org.success.payment.paypal.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncomingWebhook {
    private String id;
    private String event_version;
    private LocalDateTime create_time;
    private String resource_type;
    private String resource_version;
    private String event_type;
    private String summary;
    private Object resource;
}
