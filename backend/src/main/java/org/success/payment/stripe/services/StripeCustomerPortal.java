package org.success.payment.stripe.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;
import org.springframework.stereotype.Service;
import org.success.payment.PortalRedirect;
import org.success.payment.stripe.StripeContext;

@Service
public class StripeCustomerPortal extends StripeContext {

    public PortalRedirect createShortLivedPortal(String customerId) throws StripeException {
        Stripe.apiKey = this.privateKey;
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setCustomer(customerId)
                        .setReturnUrl(this.frontendUrl + "/account")
                        .build();
        Session session = Session.create(params);
        return new PortalRedirect(session.getUrl());
    }
}
