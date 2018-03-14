package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDao extends CrudRepository<Topic, Long> {
}
