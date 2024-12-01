package org.success.payment.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.param.PriceUpdateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public abstract class StripeContext {
    @Value("${stripe-public-key}")
    protected String publicKey;

    @Value("${stripe-private-key}")
    protected String privateKey;

    @Value("${stripe-lookup-key}")
    protected String lookupKey;

    @PostConstruct
    public void setLookupKey() throws StripeException {
        System.out.println(this.lookupKey);
        Stripe.apiKey = this.privateKey;
        Price resource = Price.retrieve("price_1QRIRfG3TGHQ3eAX84Zcz8Qu");
        PriceUpdateParams params =
                PriceUpdateParams.builder().setLookupKey(this.lookupKey).build();
        Price price = resource.update(params);
    }
}
