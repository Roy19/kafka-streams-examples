package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Order {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("item_name")
    private String itemName;

    @Override
    public String toString() {
        return "Order{" +
                ", id=" + id +
                ", userId=" + userId +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
