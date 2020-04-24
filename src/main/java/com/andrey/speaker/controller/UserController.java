package com.andrey.speaker.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.Role;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.domain.dto.MessageDTO;
import com.andrey.speaker.persistence.UserRepository;
import com.andrey.speaker.service.MessageService;
import com.andrey.speaker.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserRepository usrRepo;
	private UserService userService;
	private MessageService messageService;
	
	@Autowired
	public UserController(UserRepository usrRepo, UserService userService, MessageService messageService) {
		this.usrRepo = usrRepo;
		this.userService = userService;
		this.messageService = messageService;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView showPanel(@AuthenticationPrincipal User user) {
		ModelAndView view = new ModelAndView("userList");
		view.addObject("users", usrRepo.findAll());
		
		return view;
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView editUser(@PathVariable Long id,
								 @AuthenticationPrincipal User currentUser) {
		User user = usrRepo.findById(id).get();
		ModelAndView view = new ModelAndView("editUser");
		view.addObject("roles", Role.values());
		view.addObject("user", user);
		
		return view;
	}
	
	@PostMapping("/edit/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String processEditUser(@PathVariable("id") Long id, @RequestParam String[] roles) {
		User user = usrRepo.findById(id).get();
		userService.editUserRoles(user, roles);
		
		return "redirect:/user";
	}
	
	@GetMapping("/profile")
	public ModelAndView showProfile(@AuthenticationPrincipal User user) {
		ModelAndView view = new ModelAndView("profile");
		view.addObject("user", user);
		
		return view;
	}
	
	@PostMapping("/profile")
	public String processProfileEditting(@AuthenticationPrincipal User user,
										@RequestParam String password,
										@RequestParam String mail) {;
		
		userService.updateProfile(user, password, mail);
		
		return "redirect:/user/profile";
	}
	
	@GetMapping("/user-messages/{id}")
	public String getMessages(@AuthenticationPrincipal User currentUser,
							  @PathVariable Long id,
							  Model model,
							  @PageableDefault(sort= {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
		User user = usrRepo.findById(id).get();
		Page<MessageDTO> page = messageService.findMessagesByUser(user, pageable, currentUser);

		
		model.addAttribute("messages", page.getContent());
		model.addAttribute("page", page);
		model.addAttribute("user", user);
		model.addAttribute("subscribers", user.getSubscribers().size());
		model.addAttribute("subscriptions", user.getSubscriptions().size());
	
		return "messageList";
	}
	
	
	@GetMapping("/user-messages/edit/{id}")
	public String editMessages(@AuthenticationPrincipal User currentUser,
							  @PathVariable Long id,
							  Model model) {
		
		User user = usrRepo.findById(id).get();
		Set<Message> messages = user.getMessages();
		model.addAttribute("messages", messages);
		model.addAttribute("user", user);
		
		return "editMessage";
	}
	
	@PostMapping("/subscribe/{id}")
	public String subscribe(@AuthenticationPrincipal User currentUser,
							@PathVariable Long id) {
		User user = usrRepo.findById(id).get();
		userService.subscribe(user, currentUser);
		
		return "redirect:/user/user-messages/" + user.getId();
	}
	
	@PostMapping("/unscribe/{id}")
	public String unscribe(@AuthenticationPrincipal User currentUser,
							@PathVariable Long id) {
		User user = usrRepo.findById(id).get();
		userService.unscribe(user, currentUser);
		
		return "redirect:/user/user-messages/" + user.getId();
	}
	
	@GetMapping("/subscribers/{id}")
	public String checkSubscribers(@AuthenticationPrincipal User currentUser,
									@PathVariable Long id,
									Model model) {
		User user = usrRepo.findById(id).get();
		Iterable<User> subscribers = user.getSubscribers();
		model.addAttribute("user", user);
		model.addAttribute("users", subscribers);
				
		
		return "subs";
	}
	
	
	@GetMapping("/subscriptions/{id}")
	public String checkSubscriptions(@AuthenticationPrincipal User currentUser,
									@PathVariable Long id,
									Model model) {
		User user = usrRepo.findById(id).get();
		Iterable<User> subscriptions = user.getSubscriptions();
		model.addAttribute("user", user);
		model.addAttribute("users", subscriptions);
				
		
		return "subs";
	}
}
