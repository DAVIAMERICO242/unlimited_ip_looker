package org.success.payment.stripe.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceListParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.success.payment.stripe.DTOs.CheckoutRedirect;
import org.success.payment.stripe.StripeContext;

@Service
public class StripeCheckout extends StripeContext {

    public CheckoutRedirect createCheckoutSession() throws StripeException {//returns checkout URL
        PriceListParams priceParams =
                PriceListParams.builder().addLookupKey(this.lookupKey).build();
        PriceCollection prices = Price.list(priceParams);

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(prices.getData().get(0).getId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(frontendUrl + "?success=true&session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "?canceled=true")
                .build();
        Session session = Session.create(params);
        return new CheckoutRedirect(session.getUrl());
    }
}
