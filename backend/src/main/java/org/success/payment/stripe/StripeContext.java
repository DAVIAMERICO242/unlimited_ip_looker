package org.success.payment.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.param.PriceUpdateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public abstract class StripeContext {

    @Value("${app.frontend-url}")
    protected String frontendUrl;

    protected String publicKey;

    protected String privateKey;

    protected String lookupKey;

    protected String priceId;

    protected String webhookSecret;

    @Value("${app.on-production}")
    private Boolean onProduction;

    @Value("${stripe.dev.public-key}")
    private String devPublicKey;

    @Value("${stripe.dev.private-key}")
    private String devPrivateKey;

    @Value("${stripe.dev.lookup-key}")
    private String devLookupKey;

    @Value("${stripe.dev.price-id}")
    private String devPriceId;

    @Value("${stripe.dev.webhook-secret}")
    private String devWebhookSecret;

    @Value("${stripe.prod.public-key}")
    private String prodPublicKey;

    @Value("${stripe.prod.private-key}")
    private String prodPrivateKey;

    @Value("${stripe.prod.lookup-key}")
    private String prodLookupKey;

    @Value("${stripe.prod.price-id}")
    private String prodPriceId;

    @Value("${stripe.prod.webhook-secret}")
    private String prodWebhookSecret;

    @PostConstruct
    public void setLookupKeyOnStripeServer() throws StripeException {
        configureEnviroment();
        System.out.println(this.lookupKey);
        Stripe.apiKey = this.privateKey;
        Price resource = Price.retrieve(this.priceId);
        PriceUpdateParams params =
                PriceUpdateParams.builder().setLookupKey(this.lookupKey).build();
        Price price = resource.update(params);
    }

    private void configureEnviroment(){
        if(onProduction){
            this.publicKey = this.prodPublicKey;
            this.privateKey = this.prodPrivateKey;
            this.lookupKey = this.prodLookupKey;
            this.priceId = this.prodPriceId;
            this.webhookSecret = this.prodWebhookSecret;
        }else{
            this.publicKey = this.devPublicKey;
            this.privateKey = this.devPrivateKey;
            this.lookupKey = this.devLookupKey;
            this.priceId = this.devPriceId;
            this.webhookSecret = this.devWebhookSecret;
        }
    }
}
