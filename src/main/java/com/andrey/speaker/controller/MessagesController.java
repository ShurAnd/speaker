package com.andrey.speaker.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.User;
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
	    Page<Message> page;
		if (filter == null || filter.isEmpty()) {
			page = msgRepo.findAll(pageable);
		} else {
			page = msgRepo.findMessagesByTag(filter, pageable);
		}
		System.out.println(page.getContent());
		
		/*
		 * Определение страниц 
		 *  
		 */
		Integer[] pageNum;
		Integer[] head;
		Integer[] tail;
		Integer[] bodyBefore;
		Integer[] bodyAfter;
		Integer[] middle;
		System.out.println("Entry Point");
		if (page.getTotalPages() > 7) { 
			if(page.getNumber() + 1 > 4) { 
				head=new Integer[] {1, -1};
			}else {
				head = new Integer[] {1, 2, 3};
			}
			
			if (page.getNumber() + 1 < page.getTotalPages() - 3) {
				tail = new Integer[] {-1, page.getTotalPages()};
			}else {
				tail = new Integer[] {page.getTotalPages() - 2, page.getTotalPages() - 1, page.getTotalPages()};
			}
			
			if (page.getNumber() + 1 > 4 && page.getNumber() + 1 < page.getTotalPages() - 1) {
				System.out.println(page.getNumber() - 1 + " | " + page.getNumber());
				bodyBefore = new Integer[] {page.getNumber() - 1, page.getNumber()};
			}else {
				bodyBefore = new Integer[] {};
			}
			
			if (page.getNumber() + 1 > 2 && page.getNumber() + 1 < page.getTotalPages() - 3) {
				bodyAfter = new Integer[] {page.getNumber() + 2, page.getNumber() + 3};
			}else {
				bodyAfter = new Integer[] {};
			}
			
			if (page.getNumber() + 1 > 3 && page.getNumber() + 1 < page.getTotalPages() - 2) {
				middle = new Integer[] {page.getNumber() + 1};
			}else {
				middle = new Integer[] {};
			}
			List<Integer[]> list = new ArrayList<>();
			list.add(head);
			System.out.println(list);
			for (Integer i : head) System.out.println(i);
			list.add(bodyBefore);
			System.out.println(list);
			for (Integer i : bodyBefore) System.out.println(i);
			list.add(middle);
			System.out.println(list);
			for (Integer i : middle) System.out.println(i);
			list.add(bodyAfter);
			System.out.println(list);
			for (Integer i : bodyAfter) System.out.println(i);
			list.add(tail);
			System.out.println(list);
			for (Integer i : tail) System.out.println(i);
			
			
			pageNum = new Integer[head.length + bodyBefore.length + middle.length + bodyAfter.length + tail.length];
			list.stream().flatMap(m -> Arrays.stream(m)).collect(Collectors.toList()).toArray(pageNum);
		} else {
			pageNum = new Integer[page.getTotalPages()];
			for (int i = 0; i < pageNum.length; i++) {
				pageNum[i] = i + 1;
			}
		}
		Arrays.stream(pageNum).forEach(System.out::println);
		
		/*
		 * Определение размера
		 * 
		 */
		Integer[] sizeNum = new Integer[] {5, 10, 15, 20};
		
		view.addObject("currentUser", user);
		view.addObject("filter", filter);
		view.addObject("messages", page.getContent());
		view.addObject("pageNumbers", pageNum);
		view.addObject("pageCount", pageNum.length);
		view.addObject("currentPage", page.getNumber());
		view.addObject("sizeNumbers", sizeNum);
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
		model.addAttribute("currentUser", user);
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
		/*
		 * Определение страниц 
		 *  
		 */
		Integer[] pageNum;
		Integer[] head;
		Integer[] tail;
		Integer[] bodyBefore;
		Integer[] bodyAfter;
		Integer[] middle;
		System.out.println("Entry Point");
		if (page.getTotalPages() > 7) { 
			if(page.getNumber() + 1 > 4) { 
				head=new Integer[] {1, -1};
			}else {
				head = new Integer[] {1, 2, 3};
			}
			
			if (page.getNumber() + 1 < page.getTotalPages() - 3) {
				tail = new Integer[] {-1, page.getTotalPages()};
			}else {
				tail = new Integer[] {page.getTotalPages() - 2, page.getTotalPages() - 1, page.getTotalPages()};
			}
			
			if (page.getNumber() + 1 > 4 && page.getNumber() + 1 < page.getTotalPages() - 1) {
				System.out.println(page.getNumber() - 1 + " | " + page.getNumber());
				bodyBefore = new Integer[] {page.getNumber() - 1, page.getNumber()};
			}else {
				bodyBefore = new Integer[] {};
			}
			
			if (page.getNumber() + 1 > 2 && page.getNumber() + 1 < page.getTotalPages() - 3) {
				bodyAfter = new Integer[] {page.getNumber() + 2, page.getNumber() + 3};
			}else {
				bodyAfter = new Integer[] {};
			}
			
			if (page.getNumber() + 1 > 3 && page.getNumber() + 1 < page.getTotalPages() - 2) {
				middle = new Integer[] {page.getNumber() + 1};
			}else {
				middle = new Integer[] {};
			}
			List<Integer[]> list = new ArrayList<>();
			list.add(head);
			System.out.println(list);
			for (Integer i : head) System.out.println(i);
			list.add(bodyBefore);
			System.out.println(list);
			for (Integer i : bodyBefore) System.out.println(i);
			list.add(middle);
			System.out.println(list);
			for (Integer i : middle) System.out.println(i);
			list.add(bodyAfter);
			System.out.println(list);
			for (Integer i : bodyAfter) System.out.println(i);
			list.add(tail);
			System.out.println(list);
			for (Integer i : tail) System.out.println(i);
			
			
			pageNum = new Integer[head.length + bodyBefore.length + middle.length + bodyAfter.length + tail.length];
			list.stream().flatMap(m -> Arrays.stream(m)).collect(Collectors.toList()).toArray(pageNum);
		} else {
			pageNum = new Integer[page.getTotalPages()];
			for (int i = 0; i < pageNum.length; i++) {
				pageNum[i] = i + 1;
			}
		}
		Arrays.stream(pageNum).forEach(System.out::println);
		
		/*
		 * Определение размера
		 * 
		 */
		Integer[] sizeNum = new Integer[] {5, 10, 15, 20};
		
		model.addAttribute("currentUser", user);
		model.addAttribute("messages", page.getContent());
		model.addAttribute("pageNumbers", pageNum);
		model.addAttribute("pageCount", pageNum.length);
		model.addAttribute("currentPage", page.getNumber());
		model.addAttribute("sizeNumbers", sizeNum);
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
		model.addAttribute("currentUser", user);
		
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
	
	


}
