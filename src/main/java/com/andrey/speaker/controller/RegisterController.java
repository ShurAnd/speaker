package com.andrey.speaker.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.Role;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.persistence.UserRepository;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	private UserRepository usrRepo;
	private PasswordEncoder encoder;
	
	@Autowired
	public RegisterController(UserRepository usrRepo, PasswordEncoder encoder) {
		this.usrRepo = usrRepo;
		this.encoder = encoder;
	}

	@GetMapping
	public ModelAndView startRegistration() {
		ModelAndView view = new ModelAndView("registration");
		User user = new User();
		view.addObject("user", user);
		
		return view;
	}
	
	@PostMapping
	public ModelAndView processRegistration(@ModelAttribute User user) {
		ModelAndView view = new ModelAndView();
		User checkedUser = usrRepo.findUserByUsername(user.getUsername());
		if (checkedUser != null) {
			view.setViewName("redirect:/register");
			view.addObject("message", "User exist, pls select another username");
			return view;
		}
		user.setPassword(encoder.encode(user.getPassword()));
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		usrRepo.save(user);
		view.setViewName("redirect:/login");
		return view;
	}
}
