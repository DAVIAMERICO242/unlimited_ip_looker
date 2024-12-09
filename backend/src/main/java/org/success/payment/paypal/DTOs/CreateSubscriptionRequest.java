package org.success.payment.paypal.DTOs;

import lombok.Data;

@Data
public class CreateSubscriptionRequest{
    private String plan_id;
    private String custom_id;
    private CallbackURLS application_context;
    @Data
    public static class CallbackURLS{
        private String return_url;
        private String cancel_url;
    }
}
