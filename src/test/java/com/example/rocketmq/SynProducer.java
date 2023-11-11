package com.example.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
@SpringBootTest
public class SynProducer {
    static final String NAME_SERVER="192.168.111.128:9876";
    /**
     * 同步发送消息
     * @throws Exception
     */
    @Test
    void contextLoads() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("test-producer");
        //设置注册中心
        producer.setNamesrvAddr(NAME_SERVER);
        producer.start();
        //1. 同步发送消息，生产者发送消息到broker，broker接收到消息，broker返回发送结果给生产者，才算结束
        Message message = new Message("orderTopic", "订单".getBytes(StandardCharsets.UTF_8));
        SendResult result = producer.send(message);
        System.out.println(result.getSendStatus());
        producer.shutdown();
    }
}
