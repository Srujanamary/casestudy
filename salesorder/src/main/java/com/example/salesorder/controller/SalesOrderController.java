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
        return salesOrderService.getOrderDetailsByEmail(email);}

}