package org.success.payment.paypal.DTOs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateSubscriptionResponse {
    private String id;
    private String custom_id;
    private String status;
    private LocalDateTime create_time;
    private List<Links> links;
    @Data
    public static class Links{
        String href;
        String rel;
        String method;
    }

}
