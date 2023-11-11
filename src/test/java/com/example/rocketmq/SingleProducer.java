package com.example.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SingleProducer {
    /**
     * 单向消息
     * @throws Exception
     */
    @Test
    void singleProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("single-producer");
    }
}
