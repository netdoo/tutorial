package com.extape;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.tape2.ObjectQueue;
import com.squareup.tape2.QueueFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);



    public void complexObjectQueueSample() throws Exception {

    }

    public static void main( String[] args ) throws Exception {

        App app = new App();
        //app.basicSample();
        //app.objectQueueSample();
        app.complexObjectQueueSample();
    }
}
