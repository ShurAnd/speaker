package com.andrey.speaker.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ControllerUtils {

	public static Map<String, String> getErrors(BindingResult bindingResult){
		return bindingResult.getFieldErrors().stream().collect(Collectors.toMap((fieldError -> fieldError.getField()+"Error"),
				   (FieldError::getDefaultMessage)));
	}
}
