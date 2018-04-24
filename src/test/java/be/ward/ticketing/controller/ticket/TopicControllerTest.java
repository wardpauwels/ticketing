package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Topic;
import be.ward.ticketing.service.ticket.TopicService;
import jersey.repackaged.com.google.common.collect.Lists;
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
public class TopicControllerTest {

    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    @Test
    public void findAllTopicsTest() {
        Topic topicOne = new Topic();
        topicOne.setName("My topic");
        Topic topicTwo = new Topic();
        topicTwo.setName("My second topic");

        when(topicController.findAllTopics()).thenReturn(Lists.newArrayList(topicOne, topicTwo));

        List<Topic> topics = topicController.findAllTopics();

        assertNotNull(topics);
        assertEquals(2, topics.size());
        assertEquals(topicOne.getName(), topics.get(0).getName());
        assertEquals(topicTwo.getName(), topics.get(1).getName());
        verify(topicService, times(1)).findAllTopics();
        verifyNoMoreInteractions(topicService);
    }
}