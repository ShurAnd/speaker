package com.andrey.speaker.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

	private UserRepository usrRepo;
	
	@Autowired
	public UserController(UserRepository usrRepo) {
		this.usrRepo = usrRepo;
	}
	
	@GetMapping
	public ModelAndView showPanel() {
		ModelAndView view = new ModelAndView("userList");
		view.addObject("users", usrRepo.findAll());
		
		return view;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editUser(@PathVariable Long id) {
		User user = usrRepo.findById(id).get();
		ModelAndView view = new ModelAndView("editUser");
		view.addObject("roles", Role.values());
		view.addObject("user", user);
		
		return view;
	}
	
	@PostMapping("/edit/{id}")
	public String processEditUser(@ModelAttribute User user, @RequestParam String[] roles) {
		user.getRoles().clear();
		user.setRoles(Arrays.stream(roles).map((stringRole) -> Role.valueOf(stringRole)).collect(Collectors.toSet()));
		
		usrRepo.save(user);
		
		return "redirect:/user";
	}
}
