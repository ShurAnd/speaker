package com.andrey.speaker.service;

import org.springframework.stereotype.Service;

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
