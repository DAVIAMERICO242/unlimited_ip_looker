package org.success.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.success.customer.repositories.CustomerRepository;
import org.success.ipLooker.IpLookerKeyService;
import org.success.payment.paypal.repositories.PaypalCustomerRepository;
import org.success.payment.stripe.repositories.StripeCustomerRepository;
import org.success.user.services.UserService;

public abstract class SubscriptionContext {
    @Autowired
    protected UserService userService;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected StripeCustomerRepository stripeCustomerRepository;

    @Autowired
    protected PaypalCustomerRepository paypalCustomerRepository;

    @Autowired
    protected IpLookerKeyService ipLookerKeyService;
}
