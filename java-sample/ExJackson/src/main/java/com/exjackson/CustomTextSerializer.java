package com.exjackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Created by jhkwon78 on 2018-04-02.
 */
public class CustomTextSerializer extends StdSerializer<String> {
    public CustomTextSerializer() {
        super(String.class);
    }


    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(s);
    }
}
