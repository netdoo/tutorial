package com.exmybatis.domain.serialize;

import com.exmybatis.domain.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.List;

public class UserDeserializer extends StdDeserializer<User> {
    public UserDeserializer() {
        this(null);
    }

    public UserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        /*
        String name =  p.getCurrentName();
        String customText = p.getText();
        return (null == customText || customText.isEmpty()) ? new ArrayList<String>() : Arrays.asList(customText.split(","));
        */

        User user = new User();
        return user;
    }
}
