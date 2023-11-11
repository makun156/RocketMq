package com.example.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class AsyncProducer {
    static final String NAME_SERVER="192.168.111.128:9876";
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
}
