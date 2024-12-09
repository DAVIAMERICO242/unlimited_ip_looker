package org.success.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.success.customer.entities.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,String>, JpaSpecificationExecutor<Customer> {

    @Query("SELECT c FROM Customer c WHERE c.stripeCustomer.stripeId =: stripeId")
    Optional<Customer> findByStripeId(String stripeId);

    @Query("SELECT c FROM Customer c WHERE c.paypalCustomer.paypalId =: paypalId")
    Optional<Customer> findByPaypalId(String paypalId);

    Optional<Customer> findByEmail(String email);


}
