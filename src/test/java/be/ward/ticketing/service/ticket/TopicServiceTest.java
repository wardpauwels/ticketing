package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.TopicRepository;
import be.ward.ticketing.entities.ticketing.Topic;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    private Topic firstTopic;
    private Topic secondTopic;

    @Before
    public void setUp() {
        firstTopic = new Topic(1L);
        firstTopic.setName("My first topic");
        secondTopic = new Topic(2L);
        secondTopic.setName("Second topic");
    }

    @Test
    public void findAllTopicsTest() {
        when(topicRepository.findAll()).thenReturn(Lists.newArrayList(firstTopic, secondTopic));

        List<Topic> topics = Lists.newArrayList(topicService.findAllTopics());

        assertNotNull(topics);
        assertEquals(2, topics.size());
        assertEquals(firstTopic.getName(), topics.get(0).getName());
        assertEquals(secondTopic.getName(), topics.get(1).getName());
        verify(topicRepository, times(1)).findAll();
        verifyNoMoreInteractions(topicRepository);
    }

    @Test
    public void findTopicWithIdTest() {
        when(topicRepository.findOne(1L)).thenReturn(firstTopic);

        Topic askedTopic = topicService.findTopicWithId(1L);

        assertNotNull(askedTopic);
        assertEquals(firstTopic.getId(), askedTopic.getId());
        assertEquals(firstTopic.getName(), askedTopic.getName());
        verify(topicRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(topicRepository);
    }
}