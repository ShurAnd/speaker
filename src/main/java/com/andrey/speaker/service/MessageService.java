package com.andrey.speaker.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.andrey.speaker.persistence.MessageRepository;

@Service
public class MessageService {

	private MessageRepository messageRepository;
	
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}
	public void deleteMessageById(Long id) {
		messageRepository.deleteById(id);
		
	}
}
