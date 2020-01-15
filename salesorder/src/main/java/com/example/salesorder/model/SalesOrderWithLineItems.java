package com.example.salesorder.model;

import org.apache.catalina.LifecycleState;

import java.util.List;

public class SalesOrderWithLineItems {

    SalesOrder salesOrder;
    List<OrderLineItem> orderLineItem;

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public List<OrderLineItem> getOrderLineItem() {
        return orderLineItem;
    }

    public void setOrderLineItem(List<OrderLineItem> orderLineItem) {
        this.orderLineItem = orderLineItem;
    }
}
