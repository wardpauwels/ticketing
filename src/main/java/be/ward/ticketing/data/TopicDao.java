package be.ward.ticketing.data;

import be.ward.ticketing.entities.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDao extends CrudRepository<Topic, Long> {
}
