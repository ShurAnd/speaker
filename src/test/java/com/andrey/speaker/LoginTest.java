package com.andrey.speaker;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.andrey.speaker.controller.HomeController;

@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private HomeController homeController;
	
	@Test
	public void shouldReturnMainPage() throws Exception{
		mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("This is test project")));
	}
	
	@Test
	public void shouldRedirectToLoginPage() throws Exception{
		mockMvc.perform(get("/messages"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	public void correctLoginData() throws Exception{
		mockMvc.perform(formLogin().user("admin").password("1"))
				.andDo(print())
				.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void badCredentials() throws Exception{
		mockMvc.perform(post("/login").param("user", "123"))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
}
