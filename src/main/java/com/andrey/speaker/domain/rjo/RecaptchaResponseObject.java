package com.andrey.speaker.domain.rjo;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class RecaptchaResponseObject {

	private boolean success;
	@JsonAlias("error_codes")
	private Set<String> errorCodes;
}
