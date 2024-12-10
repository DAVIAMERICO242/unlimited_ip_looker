package org.success.payment.paypal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.success.payment.paypal.entities.PaypalCustomer;

import java.util.Optional;

public interface PaypalCustomerRepository extends JpaRepository<PaypalCustomer,String>, JpaSpecificationExecutor<PaypalCustomer> {
    Optional<PaypalCustomer> findByWebhookCustomerSignature(String signature);
}
