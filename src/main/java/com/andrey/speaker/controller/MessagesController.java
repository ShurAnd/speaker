package com.andrey.speaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.User;
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
	public ModelAndView messages(@RequestParam(name="filter", required=false, defaultValue="") String filter) {
		ModelAndView view = new ModelAndView("messages");
		Iterable<Message> messages;
		if (filter == null || filter.isEmpty()) {
			messages = msgRepo.findAll();
		} else {
			messages = msgRepo.findMessagesByTag(filter);
		}
		view.addObject("filter", filter);
		view.addObject("messages", messages);
		
		return view;
	}
	
	//adding new message to db and fetching all of them 
	
	@PostMapping
	public String processAddingMessage(
			@RequestParam String text,
			@RequestParam String tag,
			@AuthenticationPrincipal User user) {
		Message message = new Message(text, tag, user);
		msgRepo.save(message);
		
		return "redirect:/messages";
	}


}
