package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class UserOrders {
    private User user;
    private List<Order> orders;

    @JsonCreator
    public UserOrders(@JsonProperty("user") User user, 
                    @JsonProperty("orders") List<Order> orders) {
        this.user = user;
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "UserOrders{" +
                "user=" + user +
                ", orders=" + orders +
                '}';
    }
}
