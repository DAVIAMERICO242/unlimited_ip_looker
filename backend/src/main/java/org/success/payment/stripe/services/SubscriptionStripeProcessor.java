package org.success.payment.stripe.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.success.customer.entities.Customer;
import org.success.payment.SubscriptionContext;
import org.success.payment.stripe.entities.StripeCustomer;
import org.success.user.DTOs.CreatedUserWithRandomPass;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionStripeProcessor extends SubscriptionContext {

    @Transactional
    public void processAfterCheckout(String stripeCustomerId,String name,String phone){//como eu posso gerar o checkout a partir do stripe customerId nao é um problema
        try{
            Optional<Customer> customerOPT = customerRepository.findByStripeId(stripeCustomerId);
            if(customerOPT.isEmpty()){//ativar plano primeira vez
                this.activePlanFirstTime(stripeCustomerId,name,phone);
            }else{
                this.reactivePlan(customerOPT.get());
            }
        }catch (Exception e){
            System.out.println("ERRO AO SALVAR CUSTOMER STRIPE");
        }
    }

    private void activePlanFirstTime(String stripeCustomerId,String name,String phone){
        CreatedUserWithRandomPass output = userService.createUserWithRandomPass(stripeCustomerId);
        Customer customer = new Customer();
        customer.setUser(output.createdUser());
        customer.setName(name);
        customer.setEmail(stripeCustomerId);
        customer.setPhone(phone);
        customer.setToken(ipLookerKeyService.generateRandomKey());
        customer.setTokenActive(true);
        customer.setLastPayment(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        StripeCustomer stripeCustomer = new StripeCustomer();
        stripeCustomer.setStripeId(stripeCustomerId);
        stripeCustomer.setSystemCustomer(savedCustomer);
        stripeCustomerRepository.save(stripeCustomer);
    }

    private void reactivePlan(Customer customer){
        customer.setTokenActive(true);
        customer.setLastPayment(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
    }


}
