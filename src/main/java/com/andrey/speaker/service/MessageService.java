package com.andrey.speaker.service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.domain.dto.MessageDTO;
import com.andrey.speaker.persistence.MessageRepository;
import com.andrey.speaker.persistence.UserRepository;

@Service
public class MessageService {

	private MessageRepository messageRepository;
	
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}
	public void deleteMessageById(Long id) {
		messageRepository.deleteById(id);
		
	}
	public Page<MessageDTO> findMessagesByUser(User user, Pageable pageable, User currentUser) {
		Page<MessageDTO> page = messageRepository.findMessagesByAuthor(user, pageable, currentUser);
		
		return page;
	}
	public void like(Long id, User user) {
		Message message = messageRepository.findById(id).get();
		Set<User> likes = message.getLikes();
		if (likes.contains(user)) {
			likes.remove(user);
		}else {
			likes.add(user);
		}
	}
}
