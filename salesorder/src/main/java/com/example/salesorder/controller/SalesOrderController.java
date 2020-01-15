package com.example.salesorder.controller;

import com.example.customer.model.Customer;
import com.example.customer.model.CustomerDTO;
import com.example.salesorder.model.*;
import com.example.salesorder.service.CustomerService;
import com.example.salesorder.service.ItemService;
import com.example.salesorder.service.OrderLineItemService;
import com.example.salesorder.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("salesOrderController")
public class SalesOrderController {
    private SalesOrderService salesOrderService;
    private OrderLineItemService orderLineItemService;
    private CustomerService customerService;
    private ItemService itemService;
    private static final Logger LOG = Logger.getLogger(SalesOrderController.class.getName());

//    @Autowired
//    private CustomerServiceProxy customerServiceProxy;
//
//    @Autowired
//    private ItemServiceProxy itemServiceProxy;

    public SalesOrderController(SalesOrderService salesOrderService, OrderLineItemService orderLineItemService, CustomerService customerService, ItemService itemService) {
        this.customerService = customerService;
        this.salesOrderService = salesOrderService;
        this.orderLineItemService = orderLineItemService;
        this.itemService = itemService;
    }

    @PostMapping(value = "orders", produces = "application/json")
    public String createOrder(@RequestBody OrderDetails orderDetails) {return salesOrderService.createOrder(orderDetails);}

    @GetMapping(value = "orderDetailsByEmail/{email}", produces = "application/json")
    public List<SalesOrderWithLineItems> getOrderDetailsByEmail(@PathVariable String email) {

        LOG.log(Level.INFO, "You reached order by email method");
        HashMap<String, Integer> hmap = new HashMap<>();
        List<HashMap<String, Integer>> finalList = new ArrayList<>();
        List<SalesOrder> orderIdIs = this.salesOrderService.getOrderIdByEmail(email);
        System.out.println("------orderIdIs---------" + orderIdIs);

        System.out.println("---Calling salesorder Service with orderId");
        List<SalesOrderWithLineItems> salesOrderWithLineItems= new ArrayList<>();

        for (SalesOrder salesOrder : orderIdIs) {
            SalesOrderWithLineItems salesOrderWithLineItems1 = new SalesOrderWithLineItems();
            salesOrderWithLineItems1.setSalesOrder(salesOrder);
            salesOrderWithLineItems1.setOrderLineItem(this.orderLineItemService.getOrdersById(salesOrder.getId()));
            salesOrderWithLineItems.add(salesOrderWithLineItems1);
            /*hmap = (HashMap<String, Integer>) this.orderLineItemService.getOrdersById(salesOrder.getId());
            finalList.add(hmap);*/
        }
        return salesOrderWithLineItems;
    }
}