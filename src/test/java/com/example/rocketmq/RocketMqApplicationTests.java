package com.example.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class RocketMqApplicationTests {
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

    /**
     * 异步发送消息
     * @throws Exception
     */
    @Test
    void asyncProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("async-producer");
        producer.setNamesrvAddr(NAME_SERVER);
        producer.start();
        Message message = new Message("asyncTopic","异步发送消息".getBytes(StandardCharsets.UTF_8));
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步生产者发送消息失败："+throwable.getMessage());
            }
        });
        System.out.println("我比onSuccess异步API先执行");
        Thread.sleep(1000000000);
    }

    /**
     * 单向消息
     * @throws Exception
     */
    @Test
    void singleProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("single-producer");
    }
    @Test
    void test() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        consumer.setNamesrvAddr("192.168.111.128:9876");
        consumer.subscribe("orderTopic", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            for (MessageExt messageExt : list) {
                System.out.println(new String(messageExt.getBody(), StandardCharsets.UTF_8));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        Thread.sleep(1000000000);
    }
}
