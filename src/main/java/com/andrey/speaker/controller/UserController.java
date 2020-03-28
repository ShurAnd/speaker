package com.andrey.speaker.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.Role;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.persistence.UserRepository;
import com.andrey.speaker.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserRepository usrRepo;
	private UserService userService;
	
	@Autowired
	public UserController(UserRepository usrRepo, UserService userService) {
		this.usrRepo = usrRepo;
		this.userService = userService;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView showPanel() {
		ModelAndView view = new ModelAndView("userList");
		view.addObject("users", usrRepo.findAll());
		
		return view;
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ModelAndView editUser(@PathVariable Long id) {
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
		view.addObject("username", user.getUsername());
		view.addObject("mail", user.getMail());
		
		return view;
	}
	
	@PostMapping("/profile")
	public String processProfileEditting(@AuthenticationPrincipal User user,
										@RequestParam String password,
										@RequestParam String mail) {
		
		userService.updateProfile(user, password, mail);
		
		return "redirect:/user/profile";
	}
}
