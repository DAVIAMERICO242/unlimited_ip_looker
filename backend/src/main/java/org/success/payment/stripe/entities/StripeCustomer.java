package org.success.payment.stripe.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.success.customer.entities.Customer;

import java.time.LocalDateTime;

@Entity
@Table(name="stripe_customers")
@Data
public class StripeCustomer {
    @Id
    private String stripeId;
    @OneToOne
    @JoinColumn(name="system_customer_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Customer systemCustomer = null;
    private LocalDateTime createdAt = LocalDateTime.now();

}
