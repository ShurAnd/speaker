package com.andrey.speaker.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RedirectInterceptor  extends HandlerInterceptorAdapter{

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			String arguments = request.getQueryString() != null? request.getQueryString() : "";
			String url = request.getRequestURI()+"?"+arguments;
			response.addHeader("Turbolinks-Location", url);
		}
	}

}
