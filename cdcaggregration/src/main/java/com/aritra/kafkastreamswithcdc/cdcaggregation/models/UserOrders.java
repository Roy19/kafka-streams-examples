package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserOrders {
    private User user;
    private List<Order> orders = new ArrayList<>();

    @JsonCreator
    public UserOrders(@JsonProperty("user") User user, 
                    @JsonProperty("orders") List<Order> orders) {
        this.user = user;
        this.orders = orders;
    }

    public UserOrders addOrder(OrderAndUser orderAndUser) {
        this.user = orderAndUser.getUser();
        this.orders.add(orderAndUser.getOrder());
        return this;
    }

    public UserOrders removeOrder(OrderAndUser orderAndUser) {
        this.orders.remove(orderAndUser.getOrder());
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
