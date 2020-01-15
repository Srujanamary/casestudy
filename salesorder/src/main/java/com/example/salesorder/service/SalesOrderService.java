package com.example.salesorder.service;

import com.example.customer.model.CustomerDTO;
import com.example.salesorder.model.*;
import com.example.salesorder.repository.SalesOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.logging.Level;

@Service
@Slf4j
public class SalesOrderService {

    private SalesOrderRepository salesOrderRepository;
    private OrderLineItemService orderLineItemService;
    private CustomerService customerService;
    private ItemService itemService;


    public SalesOrderService(SalesOrderRepository salesOrderRepository, OrderLineItemService orderLineItemService, CustomerService customerService, ItemService itemService) {
        this.salesOrderRepository = salesOrderRepository;
        this.orderLineItemService = orderLineItemService;
        this.customerService = customerService;
        this.itemService = itemService;

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

    public String createOrder(OrderDetails orderDetails) {



        System.out.println("----Coming inside the controller---create order---------------------");

        //System.out.println("--- Checking Email is valid or not ----" + customerService.getCustomerByEmail(orderDetails.getEmail()));
        String result = "";
        String unavailableItems = "";
        CustomerDTO isCustomerAvailable = null;
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
                salesRes = add(orderDetails.getDate(), orderDetails.getEmail(), orderDetails.getDescription(), totalPrice);


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

    public List<SalesOrderWithLineItems> getOrderDetailsByEmail(String email) {
        HashMap<String, Integer> hmap = new HashMap<>();
        List<HashMap<String, Integer>> finalList = new ArrayList<>();
        List<SalesOrder> orderIdIs = getOrderIdByEmail(email);
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
