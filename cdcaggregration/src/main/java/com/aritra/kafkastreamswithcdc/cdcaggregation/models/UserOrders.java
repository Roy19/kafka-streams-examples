package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserOrders {
    public User user;
    public List<Order> orders = new ArrayList<>();

    public UserOrders(User user, List<Order> orders) {
        this.user = user;
        this.orders = orders;
    }

    public UserOrders addOrder(OrderAndUser orderAndUser) {
        this.user = orderAndUser.user;
        if (orderAndUser.order != null && orderAndUser.order.id != null && 
            orderAndUser.order.user_id != null && orderAndUser.order.item_name != null) {
            this.orders.add(orderAndUser.order);
        }
        return this;
    }

    public UserOrders removeOrder(OrderAndUser orderAndUser) {
        Iterator<Order> iterator = this.orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.id.equals(orderAndUser.order.id)) {
                iterator.remove();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "UserOrders{" +
                "user=" + user +
                ", orders=" + orders +
                '}';
    }
}
