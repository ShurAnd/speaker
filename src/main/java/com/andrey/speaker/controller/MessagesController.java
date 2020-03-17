package com.andrey.speaker.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.persistence.MessageRepository;

@Controller
@RequestMapping("/messages")
public class MessagesController {

	private MessageRepository msgRepo;
	
	@Autowired
	public MessagesController(MessageRepository msgRepo) {
		this.msgRepo = msgRepo;
	}
	
	// Fetches all messages from db
	
	@GetMapping
	public ModelAndView messages() {
		ModelAndView view = new ModelAndView("messages");
		List<Message> messages = new ArrayList<>();
		msgRepo.findAll().forEach((m) -> messages.add(m));
		view.addObject("messages", messages);
		
		return view;
	}
	
	//adding new message to db and fetching all of them 
	
	@PostMapping
	public String processAddingMessage(@RequestParam String text, @RequestParam String tag) {
		Message message = new Message(text, tag);
		msgRepo.save(message);
		
		return "redirect:/messages";
	}
	
	// filter messages by tag
	
	@PostMapping("/filter")
	public ModelAndView filterMessagesByTag(@RequestParam String filter) {
		ModelAndView view = new ModelAndView("messages");
		Iterable<Message> messages;
		if (filter != null && !filter.isEmpty()) {
			messages = msgRepo.findMessagesByTag(filter);
		} else {
			messages = msgRepo.findAll();
		}
		
		view.addObject("messages", messages);
		
		return view;
	}
}
