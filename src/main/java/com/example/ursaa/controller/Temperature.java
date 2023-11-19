package com.example.ursaa.controller;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Measurement(name = "temperature")
class Temperature {

    @Column(tag = true)
    private String location;

    @Column
    private Double value;

    @Column(timestamp = true)
    private Instant time;

    public String getLocation() {
        return location;
    }

    public Temperature setLocation(String location) {
        this.location = location;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public Temperature setValue(Double value) {
        this.value = value;
        return this;
    }

    public Instant getTime() {
        return time;
    }

    public Temperature setTime(Instant time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "location='" + location + '\'' +
                ", value=" + value +
                ", time=" + time +
                '}';
    }
}
