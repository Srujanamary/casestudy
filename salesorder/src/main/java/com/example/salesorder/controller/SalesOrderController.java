package com.example.salesorder.controller;

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
    public String createOrder(@RequestBody OrderDetails orderDetails) {

        LOG.log(Level.INFO, "You reached create order method");

        System.out.println("----Coming inside the controller---create order---------------------");

        System.out.println("--- Checking Email is valid or not ----" + customerService.getCustomerByEmail(orderDetails.getEmail()));
        String result = "";
        String unavailableItems = "";
        Customer isCustomerAvailable = null;
        boolean customerEmailAvailable = false;
//        isCustomerAvailable = customerServiceProxy.getByEmail(orderDetails.getEmail());
        isCustomerAvailable = customerService.getCustomerByEmail(orderDetails.getEmail());

        if (isCustomerAvailable.getId() == -1L)
            return "**** Oops... Customer Server is down  ******";

        if (isCustomerAvailable != null)
            customerEmailAvailable = true;

        List<Long> orderIdsList = new ArrayList<>();

//        Long orderIdCreated = null;

        HashMap<String, Integer> hmap = new HashMap<>();

        if (customerEmailAvailable) {
            List<String> orderList = orderDetails.getItemNames();
            List<String> availableList = new ArrayList<>();

            System.out.println("orderList is " + orderList + " list empty check " + orderList.size());

            if (orderList.isEmpty())
                return "**** Oops... There are no items in the list or items are currently ******";

            double totalPrice = 0.0;
            for (String order : orderList) {

                System.out.println(" order passing to order service is " + order);
                Item item = null;
                item = itemService.get(order);
                System.out.println("returned object from item service is " + item);

                if (item == null) {
                    //result = result + "**** Oops... Item \'" + order + "\' are currently unavailable ******";
                    unavailableItems = unavailableItems + " " + order;
                    continue;
                } else if (item.getId() == -1L) {
                    return "**** Oops... Item Server is down  ******";

                }

//                System.out.println("---Item Details ----" + item);

                else {
                    totalPrice = totalPrice + item.getPrice();
                    availableList.add(item.getName());
                    if (hmap.containsKey(item.getName())) {
                        int prev = hmap.get(item.getName());
                        prev++;
                        hmap.put(item.getName(), prev);
                    } else {
                        hmap.put(item.getName(), 1);
                    }
                }
            }
            SalesOrder salesRes = null;

            if (totalPrice == 0.0)
                return result;
            else
                salesRes = this.salesOrderService.add(orderDetails.getDate(), orderDetails.getEmail(), orderDetails.getDescription(), totalPrice);


            for (Map.Entry<String, Integer> entry : hmap.entrySet()) {
                System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
                OrderLineItem orderRes = this.orderLineItemService.add(entry.getKey(), salesRes.getId(), (int) entry.getValue());
            }


            result = "Order Id is " + result + salesRes.getId();

            if (unavailableItems.length() > 1)
                result = result + " & some Items are unavailable and they are " + unavailableItems;
        }
        return result;
    }

    @GetMapping(value = "orderDetailsByEmail/{email}", produces = "application/json")
    public List<HashMap<String, Integer>> getOrderDetailsByEmail(@PathVariable String email) {

        LOG.log(Level.INFO, "You reached order by email method");
        HashMap<String, Integer> hmap = new HashMap<>();
        List<HashMap<String, Integer>> finalList = new ArrayList<>();
        List<SalesOrder> orderIdIs = this.salesOrderService.getOrderIdByEmail(email);
        System.out.println("------orderIdIs---------" + orderIdIs);

        System.out.println("---Calling salesorder Service with orderId");

        for (SalesOrder salesOrder : orderIdIs) {
            hmap = (HashMap<String, Integer>) this.orderLineItemService.getOrdersById(salesOrder.getId());
            finalList.add(hmap);
        }
        return finalList;
    }
}