package com.andrey.speaker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.andrey.speaker.domain.User;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping
	public ModelAndView welcome(@AuthenticationPrincipal User user) {
		ModelAndView view = new ModelAndView("home");
		
		return view;
	}
}
