package com.andrey.speaker.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.andrey.speaker.domain.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{
	List<Message> findMessagesByTag(String tag);
}
