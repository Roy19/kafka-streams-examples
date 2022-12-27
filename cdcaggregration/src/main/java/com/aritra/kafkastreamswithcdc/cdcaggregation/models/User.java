package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class User {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("primary_contact_number")
    private String primaryContactNumber;

    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", user_name='" + userName + '\'' +
                ", primary_contact_number='" + primaryContactNumber + '\'' +
                '}';
    }
}
