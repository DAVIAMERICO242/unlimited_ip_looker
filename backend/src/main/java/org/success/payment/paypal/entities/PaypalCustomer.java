package org.success.payment.paypal.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.success.customer.entities.Customer;

import java.time.LocalDateTime;

@Entity
@Table(name="paypal_customers")
@Data
public class PaypalCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String associationId;
    private String paypalId;
    @OneToOne
    @JoinColumn(name="system_customer_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Customer systemCustomer = null;
    private LocalDateTime createdAt = LocalDateTime.now();

}