package org.success.payment.paypal.services;

import jakarta.transaction.Transactional;
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
    public void processAfterCheckout(String paypalCustomerId, String name, String email){//se esse email é diferente do login, o usuario tera outra conta
        if(paypalCustomerId==null){
            System.out.println();
            throw new RuntimeException("PAYPAL PAYER ID NULL");
        }
        try{
            if(!userService.doesThisUsernameExist(email)){//ativar plano primeira vez
                this.activePlanFirstTime(paypalCustomerId,name,email);
            }
            else{
                this.reactivePlan(email, paypalCustomerId);
            }
        }catch (Exception e){
            System.out.println("ERRO AO SALVAR CUSTOMER STRIPE");
        }
    }

    private void activePlanFirstTime(String paypalCustomerId,String name,String email){
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
        paypalCustomer.setPaypalId(paypalCustomerId);
        paypalCustomer.setSystemCustomer(savedCustomer);
        paypalCustomerRepository.save(paypalCustomer);
    }

    private void reactivePlan(String email,String paypalCustomerId) throws Exception {
        Optional<Customer> customerOPT = customerRepository.findByEmail(email);
        if(customerOPT.isEmpty()){
            throw new Exception("User with such e-mail exists but customer does not");
        }
        Customer customer = customerOPT.get();
        if(!paypalCustomerId.equals(customer.getPaypalCustomer().getPaypalId())){//problematico,
            String oldPaypalId = customer.getPaypalCustomer().getPaypalId();
            PaypalCustomer paypalCustomer = new PaypalCustomer();
            paypalCustomer.setPaypalId(paypalCustomerId);
            PaypalCustomer saved = paypalCustomerRepository.save(paypalCustomer);
            customer.setPaypalCustomer(saved);
            customerRepository.save(customer);
            paypalCustomerRepository.deleteById(oldPaypalId);
        }
        customer.setTokenActive(true);
        customer.setLastPayment(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
    }
}
