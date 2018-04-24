package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.TopicRepository;
import be.ward.ticketing.entities.ticketing.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Iterable<Topic> findAllTopics() {
        return topicRepository.findAll();
    }

    public Topic findTopicWithId(Long id) {
        return topicRepository.findOne(id);
    }
}