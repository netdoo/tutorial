package com.exprofile;

import com.exprofile.config.AppConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = {
        AppConfig.class
},
loader = AnnotationConfigContextLoader.class)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Autowired
    String myName;

    @Test
    public void testApp() throws Exception {
        logger.info("myName {}", myName);
    }
}
