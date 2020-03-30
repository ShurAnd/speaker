package com.andrey.speaker.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.User;
import com.andrey.speaker.service.ControllerUtils;
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
	public ModelAndView processRegistration(@Valid @ModelAttribute User user,
											BindingResult bindingResult) {
		ModelAndView view = new ModelAndView("registration");
		boolean passwordConfirmation = false;
		if (!user.getPassword().equals(user.getPassword2())) {
			passwordConfirmation = true;
			view.addObject("password2Error", "Password confirmed incorrectly!");
		}
		
		if (bindingResult.hasErrors() || passwordConfirmation) {
			Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
			view.getModelMap().mergeAttributes(errors);
			return view;
		}
		
		boolean resultOfAdding = userService.addUser(user);
		if (!resultOfAdding) {
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
