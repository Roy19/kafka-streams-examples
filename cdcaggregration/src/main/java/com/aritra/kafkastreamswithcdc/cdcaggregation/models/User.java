package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.aritra.kafkastreamswithcdc.cdcaggregation.enums.EventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class User {
    private EventType eventType;
    private Integer id;
    private String user_name;
    private String primary_contact_number;

    @JsonCreator
    public User(@JsonProperty("_eventType") EventType eventType,
                @JsonProperty("id") Integer id,
                @JsonProperty("user_name") String user_name,
                @JsonProperty("primary_contact_number") String primary_contact_number) {
        this.eventType = eventType == null ? EventType.UPSERT : eventType;
        this.id = id;
        this.user_name = user_name;
        this.primary_contact_number = primary_contact_number;
    }

    @Override
    public String toString() {
        return "User{" +
                "eventType=" + eventType +
                ", id=" + id +
                ", user_name='" + user_name + '\'' +
                ", primary_contact_number='" + primary_contact_number + '\'' +
                '}';
    }
}
