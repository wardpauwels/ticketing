package be.ward.ticketing.amqp.receivers;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.util.Messages;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ticketAnsweredReceiver {

    @Autowired
    private ProcessEngine processEngine;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_TICKET_ANSWERED, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_TICKET_ANSWERED))
    @Transactional
    void sendMessageTicketIsAnswered(String ticketId) {
        processEngine.getRuntimeService().createMessageCorrelation(Messages.MSG_TICKET_ANSWERED)
                .processInstanceBusinessKey(ticketId)
                .correlateWithResult();
    }
}
