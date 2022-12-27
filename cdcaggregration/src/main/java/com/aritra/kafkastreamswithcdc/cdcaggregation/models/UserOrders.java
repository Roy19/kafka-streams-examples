package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserOrders {
    @JsonProperty("user")
    private User user;
    @JsonProperty("orders")
    private List<Order> orders = new ArrayList<>();

    public UserOrders(User user, List<Order> orders) {
        this.user = user;
        this.orders = orders;
    }

    public UserOrders addOrder(OrderAndUser orderAndUser) {
        this.user = orderAndUser.getUser();
        if (orderAndUser.getOrder() != null && orderAndUser.getOrder().getId() != null && 
            orderAndUser.getOrder().getUserId() != null && orderAndUser.getOrder().getItemName() != null) {
            this.orders.add(orderAndUser.getOrder());
        }
        return this;
    }

    public UserOrders removeOrder(OrderAndUser orderAndUser) {
        Iterator<Order> iterator = this.orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getId().equals(orderAndUser.getOrder().getId())) {
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
