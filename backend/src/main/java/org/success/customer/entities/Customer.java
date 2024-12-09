package org.success.customer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.success.payment.paypal.entities.PaypalCustomer;
import org.success.payment.stripe.entities.StripeCustomer;
import org.success.user.entities.User;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="system_customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private User user;
    private String name;
    private String email;
    private String phone;
    private String token;
    private Boolean tokenActive = true;
    private LocalDateTime lastPayment;
    @OneToOne(mappedBy = "systemCustomer")
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private StripeCustomer stripeCustomer;
    @OneToOne(mappedBy = "systemCustomer")
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private PaypalCustomer paypalCustomer;
}
