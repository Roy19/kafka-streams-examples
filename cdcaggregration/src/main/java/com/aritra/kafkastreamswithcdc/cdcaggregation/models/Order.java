package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.aritra.kafkastreamswithcdc.cdcaggregation.enums.EventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Order {
    private EventType eventType;
    private Integer id;
    private Integer userId;
    private String itemName;

    @JsonCreator
    public Order(@JsonProperty("_eventType") EventType eventType,
                @JsonProperty("id") Integer id,
                @JsonProperty("user_id") Integer userId,
                @JsonProperty("item_name") String itemName) {
        this.eventType = eventType == null ? EventType.DELETE : EventType.UPSERT;
        this.id = id;
        this.userId = userId;
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "eventType=" + eventType +
                ", id=" + id +
                ", userId=" + userId +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
