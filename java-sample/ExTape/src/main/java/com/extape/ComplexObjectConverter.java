package com.extape;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.tape2.ObjectQueue;

import java.io.IOException;
import java.io.OutputStream;

public class ComplexObjectConverter implements ObjectQueue.Converter<Car> {
    private final ObjectMapper objectMapper;

    public ComplexObjectConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Car from(byte[] bytes) throws IOException {

        Car car = this.objectMapper.readValue(bytes, Car.class);
        return car;
    }

    @Override
    public void toStream(Car val, OutputStream os) throws IOException {
        this.objectMapper.writeValue(os, val);
    }
}