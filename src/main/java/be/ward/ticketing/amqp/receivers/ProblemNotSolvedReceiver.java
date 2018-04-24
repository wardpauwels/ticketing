package be.ward.ticketing.amqp.receivers;

import be.ward.ticketing.util.ticket.Constants;
import be.ward.ticketing.util.ticket.Messages;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProblemNotSolvedReceiver {

    private final ProcessEngine processEngine;

    @Autowired
    public ProblemNotSolvedReceiver(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_PROBLEM_NOT_SOLVED, durable = "true"),
            exchange = @Exchange(value = Constants.VAR_DEFAULT_EXCHANGE, type = "topic", durable = "true"),
            key = Messages.MSG_PROBLEM_NOT_SOLVED))
    @Transactional
    void sendMessageTicketIsNotSolved(Object[] paramArray) {
        Long ticketId = (Long) paramArray[0];
        String comment = (String) paramArray[1];

        processEngine
                .getRuntimeService()
                .createMessageCorrelation(Messages.MSG_PROBLEM_NOT_SOLVED)
                .setVariable(Constants.VAR_COMMENT, comment)
                .processInstanceBusinessKey(String.valueOf(ticketId))
                .correlateWithResult();
    }
}