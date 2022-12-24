package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderAndUser {
    private Order order;
    private User user;

    @JsonCreator
    public OrderAndUser(@JsonProperty("order") Order order, 
                    @JsonProperty("user") User user) {
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
