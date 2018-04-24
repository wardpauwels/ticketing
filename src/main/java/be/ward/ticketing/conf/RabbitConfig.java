package be.ward.ticketing.conf;

import be.ward.ticketing.util.ticket.Constants;
import be.ward.ticketing.util.ticket.Messages;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    String[] queues = {Messages.MSG_NEW_TICKET,
            Messages.MSG_PROBLEM_NOT_SOLVED,
            Messages.MSG_PROBLEM_SOLVED,
            Messages.MSG_RESOLVER_ADDED,
            Messages.MSG_TICKET_ANSWERED,
            Messages.MSG_CLOSE_TICKET,
            Messages.MSG_NO_RESOLVER,
            Messages.MSG_RESOLVER_NOT_FOUND};

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(Constants.VAR_DEFAULT_EXCHANGE);
    }

    @Bean
    Queue deadLetterQueue() {
        return new Queue(Constants.VAR_DEAD_LETTER_QUEUE, true);
    }

    @Bean
    Queue queue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", Constants.VAR_DEFAULT_EXCHANGE);
        args.put("x-dead-letter-routing-key", Constants.VAR_DEAD_LETTER_QUEUE);
        return new Queue(Constants.VAR_DEFAULT_QUEUE, true, false, false, args);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constants.VAR_DEFAULT_QUEUE);
    }

    @Bean
    Binding bindingDeadLetter(Queue deadLetterQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deadLetterQueue).to(exchange).with(Constants.VAR_DEAD_LETTER_QUEUE);
    }

}
