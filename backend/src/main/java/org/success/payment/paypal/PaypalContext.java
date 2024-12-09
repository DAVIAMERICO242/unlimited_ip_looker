package org.success.payment.paypal;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

public abstract class PaypalContext {
    @Value("${app.on-production}")
    private Boolean onProduction;

    protected String URL;
    protected String clientID;
    protected String secretKey;
    protected String productID;
    protected String recurringPlanID;
    protected String webhookID;

    @Value("${paypal.dev.url}")
    private String devURL;//sandbox
    @Value("${paypal.dev.client-id}")
    private String devClientID;
    @Value("${paypal.dev.secret-key}")
    private String devSecretKey;
    @Value("${paypal.dev.product-id}")
    private String devProductID;
    @Value("${paypal.dev.recurring-plan-id}")
    private String devRecurringPlanID;
    @Value("${paypal.dev.webhook-id}")
    private String devWebhookID;

    @Value("${paypal.prod.url}")
    private String prodURL;//sandbox
    @Value("${paypal.prod.client-id}")
    private String prodClientID;
    @Value("${paypal.prod.secret-key}")
    private String prodSecretKey;
    @Value("${paypal.prod.product-id}")
    private String prodProductID;
    @Value("${paypal.prod.recurring-plan-id}")
    private String prodRecurringPlanID;
    @Value("${paypal.prod.webhook-id}")
    private String prodWebhookID;

    @PostConstruct
    public void configureEnviroment(){
        if(onProduction){
            this.URL = this.prodURL;
            this.clientID = this.prodClientID;
            this.secretKey = this.prodSecretKey;
            this.productID = this.prodProductID;
            this.recurringPlanID = this.prodRecurringPlanID;
            this.webhookID = this.prodWebhookID;
        }else{
            this.URL = this.devURL;
            this.clientID = this.devClientID;
            this.secretKey = this.devSecretKey;
            this.productID = this.devProductID;
            this.recurringPlanID = this.devRecurringPlanID;
            this.webhookID = this.devWebhookID;
        }
    }


}
