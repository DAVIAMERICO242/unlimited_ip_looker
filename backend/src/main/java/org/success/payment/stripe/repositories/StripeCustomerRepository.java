package org.success.payment.stripe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.success.payment.stripe.entities.StripeCustomer;

public interface StripeCustomerRepository extends JpaRepository<StripeCustomer,String>, JpaSpecificationExecutor<StripeCustomer> {
}
