package com.example.rocketmq.demo.demo1;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {
    public Consumer() {
    }

    /**
     * 当前例子是 PushConsumer 用法，给用户感觉是消息从 RocketMQ 服务器推到了应用客户端。
     * 而实际 PushConsumer 内部是使用长轮询 Pull(拉取) 方式从 MetaQ 服务器拉消息，然后再回调用户 Listener方法
     *
     * @param args
     * @throws InterruptedException
     * @throws MQClientException
     */
    public static void main(String[] args) throws InterruptedException, MQClientException {

        /**
         * 声明并初始化 一个 consumer
         * Consumer Group 组名，多个 Consumer 如果属于一个应用，订阅同样的消息，且消费逻辑一致，则应该将它们归为同一组
         * 一个应用创建一个 Consumer，由应用来维护此对象，可以设置为全局对象或者单例
         * ConsumerGroupName 需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroupName");
        /**
         * 指定 NameServer 的地址 与端口
         * 指定自己在 Consumer Group 组中的名称
         */
        consumer.setNamesrvAddr("192.168.70.74:9876;192.168.70.91;9876");
        consumer.setInstanceName("consumber");
        /**
         * 设置 consumer 的消费策略
         * CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
         * CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
         * CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和 setConsumeTimestamp() 配合使用，默认是半个小时以前
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        /**
         * 消费者订阅消息，如下所示订阅 topic 为 TopicTest2 下的所有 tag 类型 的消息
         * 订阅指定 topic下 tags 分别等于TagA或TagB或TagC，则为：consumer.subscribe("TopicTest", "TagA || TagB || TagC");
         * 一个 consumer 对象可以订阅多个 topic
         */
        consumer.subscribe("TopicTest2", "*");

        /**
         * 注册消息监听器，如果有订阅的消息就会响应
         */
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            /**
             * 默认 msgs 里只有一条消息，可以通过设置 consumeMessageBatchMaxSize 参数来批量接收消息
             * consumeThreadMin:消费线程池数量 默认最小值10
             * consumeThreadMax:消费线程池数量 默认最大值20
             */
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());
                MessageExt msg = msgs.get(0);
                System.out.printf("%s Receive New Messages: %s %n", new Object[]{Thread.currentThread().getName(), msgs});
                System.out.println("----------:" + new String(msg.getBody()));
                /**
                 * 返回消费状态
                 * CONSUME_SUCCESS 消费成功
                 * RECONSUME_LATER 消费失败，需要稍后重新消费
                 */
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        /**
         * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         */
        consumer.start();
        System.out.printf("Consumer Started.%n", new Object[0]);
    }

}
