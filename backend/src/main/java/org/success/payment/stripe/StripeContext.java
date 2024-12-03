package org.success.payment.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.param.PriceUpdateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public abstract class StripeContext {

    protected String publicKey;

    protected String privateKey;

    protected String lookupKey;

    @Value("${app.on-production}")
    private Boolean onProduction;

    @Value("${stripe.dev.public-key}")
    private String devPublicKey;

    @Value("${stripe.dev.private-key}")
    private String devPrivateKey;

    @Value("${stripe.dev.lookup-key}")
    private String devLookupKey;

    @Value("${stripe.prod.public-key}")
    private String prodPublicKey;

    @Value("${stripe.prod.private-key}")
    private String prodPrivateKey;

    @Value("${stripe.prod.lookup-key}")
    private String prodLookupKey;


    @PostConstruct
    public void setLookupKey() throws StripeException {
        getContext();
        System.out.println(this.lookupKey);
        Stripe.apiKey = this.privateKey;
        Price resource = Price.retrieve("price_1QRIRfG3TGHQ3eAX84Zcz8Qu");
        PriceUpdateParams params =
                PriceUpdateParams.builder().setLookupKey(this.lookupKey).build();
        Price price = resource.update(params);
    }

    private void getContext(){
        if(onProduction){
            this.publicKey = this.prodPublicKey;
            this.privateKey = this.prodPrivateKey;
            this.lookupKey = this.prodLookupKey;
        }else{
            this.publicKey = this.devPublicKey;
            this.privateKey = this.devPrivateKey;
            this.lookupKey = this.devLookupKey;
        }
    }
}
