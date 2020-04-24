package com.andrey.speaker.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.User;
import com.andrey.speaker.domain.rjo.RecaptchaResponseObject;
import com.andrey.speaker.service.ControllerUtils;
import com.andrey.speaker.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	private final static String recaptchaUrl="https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
	
	private UserService userService;
	private RestTemplate rest;
	@Value("${recaptcha.key}")
	private String secretKey;
	
	@Autowired
	public RegisterController(UserService userService, RestTemplate rest) {
		this.userService = userService;
		this.rest = rest;
	}

	@GetMapping
	public ModelAndView startRegistration() {
		ModelAndView view = new ModelAndView("registration");
		
		return view;
	}
	
	@PostMapping
	public ModelAndView processRegistration(
											@RequestParam("g-recaptcha-response") String recaptchaResponse,
											@RequestParam("password2") String password2,
											@Valid @ModelAttribute User user,
											BindingResult bindingResult) {
		ModelAndView view = new ModelAndView("registration");
		
		
		RecaptchaResponseObject response = rest.postForObject(String.format(recaptchaUrl, secretKey, recaptchaResponse), null, RecaptchaResponseObject.class);
		
		if (!response.isSuccess()) {
			view.addObject("recaptchaError", "fill captcha");
		}
		
		boolean passwordConfirmationProblems = false;
		if (StringUtils.isEmpty(password2)) {
			passwordConfirmationProblems = true;
			view.addObject("password2Error", "password confirmation cant be empty");
		}
		if (!passwordConfirmationProblems && !user.getPassword().equals(password2)) {
			passwordConfirmationProblems = true;
			view.addObject("password2Error", "Password confirmed incorrectly!");
		}
		
		if (bindingResult.hasErrors() || passwordConfirmationProblems || !response.isSuccess()) {
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
