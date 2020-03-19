package com.andrey.speaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.User;
import com.andrey.speaker.persistence.UserRepository;

@Controller
@RequestMapping("/user")
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
	public ModelAndView editUser(@PathVariable User user) {
		ModelAndView view = new ModelAndView("editUser");
		view.addObject("user", user);
		
		return view;
	}
}
