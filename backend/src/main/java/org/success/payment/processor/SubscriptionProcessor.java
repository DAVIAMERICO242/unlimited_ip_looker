package org.success.payment.processor;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.success.customer.entities.Customer;
import org.success.customer.repositories.CustomerRepository;
import org.success.ipLooker.IpLookerKeyService;
import org.success.payment.stripe.entities.StripeCustomer;
import org.success.payment.stripe.repositories.StripeCustomerRepository;
import org.success.user.DTOs.CreatedUserWithRandomPass;
import org.success.user.services.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionProcessor extends SubscriptionContext {

    @Transactional
    public void processStripeAfterCheckout(String stripeCustomerId,String name,String email,String phone){//como eu posso gerar o checkout a partir do stripe customerId nao é um problema
        try{
            Optional<Customer> customerOPT = customerRepository.findByStripeId(stripeCustomerId);
            if(customerOPT.isEmpty()){//ativar plano primeira vez
                this.activePlanFirstTime(stripeCustomerId,name,email,phone);
            }else{
                this.reactivePlan(customerOPT.get());
            }
        }catch (Exception e){
            System.out.println("ERRO AO SALVAR CUSTOMER STRIPE");
        }
    }

    private void activePlanFirstTime(String stripeCustomerId,String name,String email,String phone){
        CreatedUserWithRandomPass output = userService.createUserWithRandomPass(email);
        Customer customer = new Customer();
        customer.setUser(output.createdUser());
        customer.setName(name);
        customer.setEmail(email);
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
