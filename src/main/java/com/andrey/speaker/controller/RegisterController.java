package com.andrey.speaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.User;
import com.andrey.speaker.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	private UserService userService;
	
	@Autowired
	public RegisterController(UserService userService) {
		this.userService = userService;
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
		boolean resultOfAdding = userService.addUser(user);
		if (!resultOfAdding) {
			view.setViewName("redirect:/register");
			view.addObject("message", "User exist, pls select another username");
		} else {
			view.setViewName("redirect:/login");
		}

		return view;
	}
	
	@GetMapping("/activation/{code}")
	public ModelAndView activate(@PathVariable String code) {
		ModelAndView view = new ModelAndView("login");
		boolean isActivated = userService.activateUser(code);
		
		if (isActivated) {
			view.addObject("message", "Code successfully activated!");
		}else {
			view.addObject("message", "Activation code not found");
		}
		
		return view;
	}
}
