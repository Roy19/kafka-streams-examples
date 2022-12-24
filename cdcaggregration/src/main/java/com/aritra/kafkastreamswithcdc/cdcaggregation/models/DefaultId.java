package com.aritra.kafkastreamswithcdc.cdcaggregation.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class DefaultId {
    private Integer id;

    @JsonCreator
    public DefaultId(@JsonProperty("id") Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DefaultId{" +
                "id=" + id +
                '}';
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultId defaultId = (DefaultId) o;

        return id != null ? id.equals(defaultId.id) : defaultId.id == null;
    }
}
