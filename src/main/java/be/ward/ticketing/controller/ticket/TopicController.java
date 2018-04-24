package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Topic;
import be.ward.ticketing.service.ticket.TopicService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<Topic> findAllTopics() {
        return Lists.newArrayList(topicService.findAllTopics());
    }
}