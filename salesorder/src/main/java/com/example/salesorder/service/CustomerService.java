package com.example.salesorder.service;


import com.example.customer.model.Customer;
import com.example.customer.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.List;

@Service
public class CustomerService {
    private CustomerServiceProxy customerServiceProxy;

    public CustomerService(CustomerServiceProxy customerServiceProxy) {
        this.customerServiceProxy=customerServiceProxy;
    }

    @HystrixCommand(fallbackMethod="defaultCustomerService")
     public CustomerDTO getCustomerByEmail(String email){
        System.out.println("Inside the customer service---------");
        List<CustomerDTO> customer = customerServiceProxy.getCustomerByEmail(email);
        System.out.println("Inside the customer service---------");

        return customer.get(0);
    }

     private CustomerDTO defaultCustomerService(String email){
         CustomerDTO customer=new CustomerDTO();
        System.out.println("Came to default method");
        customer.setEmail(email);
        customer.setId(-1L);

        System.out.println("The default id set is " + customer.getId());
        return customer;

     }

}
