package com.andrey.speaker.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.domain.dto.MessageDTO;
import com.andrey.speaker.persistence.MessageRepository;
import com.andrey.speaker.service.ControllerUtils;
import com.andrey.speaker.service.MessageService;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Controller
@RequestMapping("/messages")
public class MessagesController {

	private MessageRepository msgRepo;
	private MessageService messageService;
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	public MessagesController(MessageRepository msgRepo, MessageService messageService) {
		this.msgRepo = msgRepo;
		this.messageService = messageService;
	}
	
	// Fetches all messages from db
	
	@GetMapping
	public ModelAndView messages(
			@AuthenticationPrincipal User user,
			@RequestParam(name="filter", required=false, defaultValue="") String filter,
			@PageableDefault(sort= {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
		ModelAndView view = new ModelAndView("messages");
	    Page<MessageDTO> page;
		if (filter == null || filter.isEmpty()) {
			page = msgRepo.findAll(pageable, user);
		} else {
			page = msgRepo.findMessagesByTag(filter, pageable, user);
		}
		
		view.addObject("filter", filter);
		view.addObject("page", page);
		view.addObject("messages", page.getContent());
		view.addObject("currentPage", page.getNumber());
		view.addObject("currentSize", page.getSize());
		
		return view;
	}
	
	//adding new message to db and fetching all of them 
	
	@PostMapping
	public String processAddingMessage(
			@Valid Message message,
			BindingResult bindingResult,
			@AuthenticationPrincipal User user,
			Model model,
			@RequestParam MultipartFile file,
			@PageableDefault(sort= {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws IllegalStateException, IOException {
		
		
		message.setAuthor(user);
		model.addAttribute("message", message);
		if (bindingResult.hasErrors()) {
			System.out.println("inside binding results");
			Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errors);
		}else {
			System.out.println("outside binding results");
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
		model.addAttribute("message", null);
		}
		Page<Message> page = msgRepo.findAll(pageable);
		
		
		model.addAttribute("page", page);
		model.addAttribute("messages", page.getContent());
		model.addAttribute("currentPage", page.getNumber());
		model.addAttribute("currentSize", page.getSize());
		return "messages";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteMessageById(@PathVariable Long id) {
		messageService.deleteMessageById(id);
		
		return "redirect:/messages";
	}
	
	@GetMapping("/edit/{id}")
	public String editMessageById(@PathVariable Long id,
								  @AuthenticationPrincipal User user,
								  Model model) {
		Message message = msgRepo.findById(id).get();
		model.addAttribute("message", message);
		
		return "editMessage";
	}
	
	@PostMapping("/edit/{id}")
	public String processEdittingMessage(@AuthenticationPrincipal User user,
										@Valid Message message,
										BindingResult bindingResult,
										Model model,
										@PathVariable Long id,
										@RequestParam MultipartFile file) throws IllegalStateException, IOException {
		model.addAttribute("currentUser", user);
		Message oldMessage = msgRepo.findById(id).get();
		if (bindingResult.hasErrors()) {
			System.out.println("inside binding results");
			Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errors);
			model.addAttribute("message", oldMessage);
			return "editMessage";
		}else {
			System.out.println("outside binding results");
		if (file != null && !file.getOriginalFilename().isEmpty() && !oldMessage.getFilename().equals(message.getFilename())) {
			
			File upload = new File(uploadPath);
			if (!upload.exists()) {
				upload.mkdir();
			}
			
			String uuid = UUID.randomUUID().toString();
			String resultFileName = uuid + file.getOriginalFilename();
			file.transferTo(new File(upload + "/" + resultFileName));
			oldMessage.setFilename(resultFileName);
		}
		oldMessage.setText(message.getText());
		oldMessage.setTag(message.getTag());
		}
		
		msgRepo.save(oldMessage);
		
		
		
		return "redirect:/messages/edit/"+id;
	}
	
	//like message
	
	@GetMapping("/like/{message_id}")
	public String like_unlike(@PathVariable("message_id") Long id,
							@AuthenticationPrincipal User user,
							RedirectAttributes attributes,
							@RequestHeader(required=false) String referer) {
		
		messageService.like(id, user);
		
		UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
		components.getQueryParams().entrySet().forEach(pair -> attributes.addAttribute(pair.getKey(), pair.getValue()));
		
		return "redirect:" + components.getPath();
	}


}
