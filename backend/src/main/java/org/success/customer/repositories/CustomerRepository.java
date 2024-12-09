package org.success.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.success.customer.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer,String>, JpaSpecificationExecutor<Customer> {
}
