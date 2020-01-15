package com.example.customer.service;

import com.example.customer.model.Customer;
import com.example.customer.model.CustomerDTO;
import com.example.customer.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public Customer add(CustomerDTO customerDTO){

        Customer customer1 = new Customer();
        customer1.setEmail(customerDTO.getEmail());
        customer1.setFirstName(customerDTO.getFirstName());
        customer1.setLastName(customerDTO.getLastName());
        return customerRepo.save(customer1);
    }


    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers=new ArrayList<>();
//        this.customerRepo.findAll().forEach(Customer -> customers.add(Customer));
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer: this.customerRepo.findAll()){
            populateCustDTO(customerDTOS, customer);
        }
        return customerDTOS;
       // return customers;
    }

    public List<CustomerDTO> getCustomerByEmail(String email){
        List<Customer> customers = this.customerRepo.getCustomerByEmail(email);
       /* if(customers.size() ==1) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setEmail(customers.get(0).getEmail());
            customerDTO.setFirstName(customers.get(0).getFirstName());
            customerDTO.setLastName(customers.get(0).getLastName());
            return customerDTO;
        }*/
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer: customers){
            populateCustDTO(customerDTOS, customer);
        }
        return customerDTOS;
    }

    private void populateCustDTO(List<CustomerDTO> customerDTOS, Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTOS.add(customerDTO);
    }
}
