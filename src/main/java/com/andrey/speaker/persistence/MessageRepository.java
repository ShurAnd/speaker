package com.andrey.speaker.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.domain.dto.MessageDTO;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{
	Page<Message> findMessagesByTag(String tag, Pageable pageable);
	Page<Message> findAll(Pageable pageable);
	
	@Query(
			"select new com.andrey.speaker.domain.dto.MessageDTO(m, count(ml), sum(case when ml = :user then 1 else 0 end) > 0) "
			+ "from Message m left join m.likes ml "
			+ " where m.author = :author "
			+ " group by m "
			)
	Page<MessageDTO> findMessagesByAuthor(@Param("author")User author, Pageable pageable, @Param("user")User user);
	
	@Query(
			"select new com.andrey.speaker.domain.dto.MessageDTO(m, count(ml), sum(case when ml = :user then 1 else 0 end) > 0) "
			+ "from Message m left join m.likes ml "
			+ "group by m "
			)
	Page<MessageDTO> findAll(Pageable pageable, @Param("user")User user);
	@Query(
			"select new com.andrey.speaker.domain.dto.MessageDTO(m, count(ml), sum(case when ml = :user then 1 else 0 end) > 0) "
			+ "from Message m left join m.likes ml "
			+ "where m.tag = :tag "
			+ "group by m "
			)
	Page<MessageDTO> findMessagesByTag(@Param("tag")String tag, Pageable pageable, @Param("user")User user);
}
