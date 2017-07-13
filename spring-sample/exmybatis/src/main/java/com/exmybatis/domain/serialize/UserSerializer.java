package com.exmybatis.domain.serialize;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserSerializer extends StdSerializer<String> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(
            String value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        if (value == null || value.isEmpty()) {
            gen.writeObject(new ArrayList<String>());
        } else {
            gen.writeObject(value.split(","));
        }
    }
}
