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

    @Data
    public static class SubscriptionResource{
        private String custom_id;
        private Subscriber subscriber;

        @Data
        public static class Subscriber{
            private String payer_id;
            private Name name;
            private String email_address;
            @Data
            public static class Name{
                private String given_name;
                private String surname;
            }
        }
    }
}
