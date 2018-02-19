package com.exrabbitmq;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);
    final static String helloWorldQueueName = "hello.world.queue";
    final static AmqpTemplate ampqTemplate = createAmpqTemplate();

    static RabbitTemplate createAmpqTemplate() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
        amqpAdmin.declareQueue(new Queue(helloWorldQueueName));

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(helloWorldQueueName);
        template.setQueue(helloWorldQueueName);

        return template;
    }

    @BeforeClass
    public static void 테스트_준비() throws Exception {

    }

    @Test
    public void _01_Producer_테스트() {
        ampqTemplate.convertAndSend("Hello World");
    }

    @Test
    public void _02_Consumer_테스트() {
        String message = (String)ampqTemplate.receiveAndConvert();
        logger.info("recv {}", message);
    }
}
