package com.andrey.speaker.ut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.persistence.MessageRepository;
import com.andrey.speaker.service.MessageService;

public class MessageServiceTests {
	
	private MessageRepository msgRepo = Mockito.mock(MessageRepository.class);
	private MessageService messageService = new MessageService(msgRepo);
	

	@Test
	public void shouldDeleteMessageById() {
		Message msg = new Message();
		msg.setId(1L);
		
		messageService.deleteMessageById(msg.getId());
		
		Mockito.verify(msgRepo, Mockito.times(1)).deleteById(1L);
	}
}
