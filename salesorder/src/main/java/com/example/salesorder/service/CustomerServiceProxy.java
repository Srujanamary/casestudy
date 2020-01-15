package com.example.salesorder.service;


import com.example.customer.model.Customer;
import com.example.customer.model.CustomerDTO;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RibbonClient(name="customer-service")
@FeignClient(name="customer-service")
public interface CustomerServiceProxy {
    @GetMapping("customers/getCustomerByEmail/{email}")
    List<CustomerDTO> getCustomerByEmail(@PathVariable("email") String email);

}
