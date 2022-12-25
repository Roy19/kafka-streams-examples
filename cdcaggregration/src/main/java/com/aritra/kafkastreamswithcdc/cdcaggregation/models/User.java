package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    public Integer id;
    public String user_name;
    public String primary_contact_number;

    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", user_name='" + user_name + '\'' +
                ", primary_contact_number='" + primary_contact_number + '\'' +
                '}';
    }
}
