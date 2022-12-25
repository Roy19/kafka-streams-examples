package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderAndUser {
    public Order order;
    public User user;

    public OrderAndUser(Order order, User user) {
        this.order = order;
        this.user = user;
    }

    @Override
    public String toString() {
        return "OrderAndUser{" +
                "order=" + order +
                ", user=" + user +
                '}';
    }
}
