package com.andrey.speaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping
	public ModelAndView welcome(@RequestParam(value = "name", required = false, defaultValue="World") String name) {
		ModelAndView view = new ModelAndView("home");
		view.addObject("name", name);
		
		return view;
	}
}
