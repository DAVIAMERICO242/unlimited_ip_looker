package org.success.payment.paypal.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.success.customer.entities.Customer;
import org.success.payment.SubscriptionContext;
import org.success.payment.paypal.entities.PaypalCustomer;
import org.success.payment.paypal.repositories.PaypalCustomerRepository;
import org.success.payment.stripe.entities.StripeCustomer;
import org.success.user.DTOs.CreatedUserWithRandomPass;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionPaypalProcessor extends SubscriptionContext {


    @Transactional
    public void processAfterCheckout(String webhookCustomerSignature,
                                     String paypalCustomerId,
                                     String name,
                                     String email
                                     ){//se esse email é diferente do login, o usuario tera outra conta

        Optional<PaypalCustomer> paypalCustomerOPT = paypalCustomerRepository.findByWebhookCustomerSignature(webhookCustomerSignature);
        try{
            if(paypalCustomerOPT.isEmpty()){//ativar plano primeira vez
                this.activePlanFirstTime(webhookCustomerSignature,paypalCustomerId,name,email);
            }
            else{
                this.reactivePlan(paypalCustomerOPT.get(), paypalCustomerId);
            }
        }catch (Exception e){
            System.out.println("ERRO AO SALVAR CUSTOMER STRIPE");
        }
    }

    private void activePlanFirstTime(String webhookCustomerSignature, String paypalCustomerId,String name,String email){
        CreatedUserWithRandomPass output = userService.createUserWithRandomPass(email);

        Customer customer = new Customer();
        customer.setUser(output.createdUser());
        customer.setName(name);
        customer.setEmail(email);
        customer.setToken(ipLookerKeyService.generateRandomKey());
        customer.setTokenActive(true);
        customer.setLastPayment(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);

        PaypalCustomer paypalCustomer = new PaypalCustomer();
        paypalCustomer.setWebhookCustomerSignature(webhookCustomerSignature);
        paypalCustomer.setPaypalId(paypalCustomerId);
        paypalCustomer.setSystemCustomer(savedCustomer);
        paypalCustomerRepository.save(paypalCustomer);
    }

    private void reactivePlan(PaypalCustomer paypalCustomer,String paypalCustomerId) throws Exception {
        if(!paypalCustomerId.equals(paypalCustomer.getPaypalId())){//comprou com outra conta do paypal
            paypalCustomer.setPaypalId(paypalCustomerId);
            paypalCustomer = paypalCustomerRepository.save(paypalCustomer);
        }
        Customer customer = paypalCustomer.getSystemCustomer();
        customer.setTokenActive(true);
        customer.setLastPayment(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
    }
}
