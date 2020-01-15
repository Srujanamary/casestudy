package com.example.salesorder.service;

import com.example.salesorder.model.*;
import com.example.salesorder.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SalesOrderService {

    private SalesOrderRepository salesOrderRepository;

    public SalesOrderService(SalesOrderRepository salesOrderRepository) {

        this.salesOrderRepository = salesOrderRepository;
    }

    public SalesOrder add(Date Date, String Email, String Description, Double Price) {

        System.out.println("----Inside the sales order service -----");
        System.out.println();
        SalesOrder salesOrder = new SalesOrder();

        salesOrder.setPrice(Price);
        salesOrder.setEmail(Email);
        salesOrder.setDescription(Description);
        salesOrder.setDate(Date);

        System.out.println("---salesOrder -----" + salesOrder);
        return this.salesOrderRepository.save(salesOrder);


    }

    public List<SalesOrder> getOrderIdByEmail(String email)
    {
        List<SalesOrder> salesOrder = null;
        salesOrder = this.salesOrderRepository.findAllByEmail(email);


        return salesOrder;
    }

}
