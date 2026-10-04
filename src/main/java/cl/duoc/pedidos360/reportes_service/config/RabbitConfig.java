package cl.duoc.pedidos360.reportes_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDERS_TOPIC_EXCHANGE = "orders.topic";
    public static final String ORDER_WILDCARD_ROUTING_KEY = "order.#";
    public static final String REPORTS_QUEUE = "reports-queue";
    public static final String REPORTS_QUEUE_DLQ = "reports-queue.dlq";
    public static final String DLX_EXCHANGE = "dlx.exchange";

    @Bean
    public TopicExchange ordersTopicExchange() {
        return new TopicExchange(ORDERS_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE, true, false);
    }

    @Bean
    public Queue reportsQueue() {
        return QueueBuilder.durable(REPORTS_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", REPORTS_QUEUE_DLQ)
                .build();
    }

    @Bean
    public Queue reportsQueueDlq() {
        return QueueBuilder.durable(REPORTS_QUEUE_DLQ).build();
    }

    @Bean
    public Binding reportsQueueBinding(Queue reportsQueue, TopicExchange ordersTopicExchange) {
        return BindingBuilder.bind(reportsQueue).to(ordersTopicExchange).with(ORDER_WILDCARD_ROUTING_KEY);
    }

    @Bean
    public Binding reportsQueueDlqBinding(Queue reportsQueueDlq, DirectExchange dlxExchange) {
        return BindingBuilder.bind(reportsQueueDlq).to(dlxExchange).with(REPORTS_QUEUE_DLQ);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setAcknowledgeMode(org.springframework.amqp.core.AcknowledgeMode.MANUAL);
        return factory;
    }
}
