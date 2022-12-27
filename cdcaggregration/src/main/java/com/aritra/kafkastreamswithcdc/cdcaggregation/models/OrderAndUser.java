package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderAndUser {
    @JsonProperty("order")
    private Order order;
    @JsonProperty("user")
    private User user;

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
