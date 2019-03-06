package com.example.rocketmq.demo.demo1;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class Producer {
    public Producer() {
    }

    public static void main(String[] args) throws MQClientException, InterruptedException {
        /**
         * 声明并初始化一个 producer,同时指定 Producer Group 的名称
         * 一个应用创建一个 Producer，由应用来维护此对象，可以设置为全局对象或者单例
         *  Producer Group 的名称 "producerGroupName" 需要由应用来保证唯一性
         * ProducerGroup 这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个 Group 下的任意一个 Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer("producerGroupName");
        /**
         * 指定 Producer 连接的 nameServer 服务器所在地址以及端口
         * 如果是分布式部署的多个，则用分号隔开，如：
         * setNamesrvAddr("172.16.235.77:9876;172.16.235.78:9876");
         * 这里只是为了方便才将地址与端口写死，实际中应该至少放在配置文件中去
         */
        producer.setNamesrvAddr("192.168.70.74:9876");
        /**
         * 指定自己的在 Producer Group 中的 名称
         */
        producer.setInstanceName("producer");

        /**
         * Producer 对象在使用之前必须要调用 start 进行启动初始化
         * 初始化一次即可，切忌不可每次发送消息时，都调用start方法
         */
        producer.setVipChannelEnabled(false);
        producer.start();

        /**
         * 一个 Producer 对象可以发送多个 topic（主题），多个 tag 的消息
         * 本实例 send 方法采用同步调用，只要不抛异常就标识成功
         */
        for (int i = 0; i < 10; ++i) {
            try {
                Message message = null;
                if (i < 5) {
                    message = new Message("TopicTest1", "TagA", ("Hello RocketMQ " + i).getBytes("UTF-8"));
                } else {
                    message = new Message("TopicTest2", "TagB", ("我爱你,中国 " + i).getBytes("UTF-8"));
                }

                SendResult sendResult = producer.send(message);
                System.out.printf("%s%n", new Object[]{sendResult});
            } catch (Exception var5) {
                var5.printStackTrace();
                Thread.sleep(1000L);
            }
        }
        /**
         * 应用退出时，调用 shutdown 关闭网络连接，清理资源，从 MocketMQ 服务器上注销自己
         * 建议应用在 JBOSS、Tomcat 等容器的退出钩子里调用 shutdown 方法
         */
        producer.shutdown();
    }
}
