package com.example.salesorder.service;


import com.example.salesorder.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class CustomerService {
    private CustomerServiceProxy customerServiceProxy;

    public CustomerService(CustomerServiceProxy customerServiceProxy) {
        this.customerServiceProxy=customerServiceProxy;
    }

    @HystrixCommand(fallbackMethod="defaultCustomerService")
     public Customer getCustomerByEmail(String email){
        System.out.println("Inside the customer service---------");
        return customerServiceProxy.getCustomerByEmail(email);
    }

     private Customer defaultCustomerService(String email){
        Customer customer=new Customer();
        System.out.println("Came to default method");
        customer.setEmail(email);
        customer.setId(-1L);

        System.out.println("The default id set is " + customer.getId());
        return customer;

     }

}
