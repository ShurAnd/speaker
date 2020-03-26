package com.andrey.speaker.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.persistence.MessageRepository;

@Controller
@RequestMapping("/messages")
public class MessagesController {

	private MessageRepository msgRepo;
	@Value("${upload.path}")
	private String uploadPath;
	
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
			@RequestParam MultipartFile file,
			@AuthenticationPrincipal User user) throws IllegalStateException, IOException {
		Message message = new Message(text, tag, user);
		
		
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			
			File upload = new File(uploadPath);
			if (!upload.exists()) {
				upload.mkdir();
			}
			
			String uuid = UUID.randomUUID().toString();
			String resultFileName = uuid + file.getOriginalFilename();
			file.transferTo(new File(upload + "/" + resultFileName));
			message.setFilename(resultFileName);
		}
		msgRepo.save(message);
		
		return "redirect:/messages";
	}


}
