package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    public Integer id;
    public Integer user_id;
    public String item_name;

    @Override
    public String toString() {
        return "Order{" +
                ", id=" + id +
                ", userId=" + user_id +
                ", itemName='" + item_name + '\'' +
                '}';
    }
}
