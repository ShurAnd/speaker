package com.andrey.speaker;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.andrey.speaker.controller.MessagesController;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("qwe")
@TestPropertySource("/application-test.properties")
@Sql(value= {"/create_user_before.sql", "/message_list_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value= {"/message_list_after.sql", "/create_user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MessageControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MessagesController homeController;
	
	@Test
	public void messagesPageAfterAuthenticationTest() throws Exception{
		mockMvc.perform(get("/messages"))
				.andDo(print())
				.andExpect(authenticated())
				.andExpect(xpath("//div[@id='navbarNavDropdown']/div[1]/span").string("qwe"));
	}
	
	@Test
	public void messageListTest() throws Exception{
		mockMvc.perform(get("/messages"))
				.andDo(print())
				.andExpect(authenticated())
				.andExpect(xpath("//div[@id='message-list']/div").nodeCount(4));
	}
	
	@Test
	public void filterMessageTest() throws Exception{
		mockMvc.perform(get("/messages").param("filter", "tag2"))
				.andDo(print())
				.andExpect(xpath("//div[@id='message-list']/div").nodeCount(2))
				.andExpect(xpath("//div[@id='message-list']/div/div[@data-id=2]").exists())
				.andExpect(xpath("//div[@id='message-list']/div/div[@data-id=4]").exists());
	}
	
	@Test
	public void addMessageTest() throws Exception {
		mockMvc.perform(multipart("/messages")
							.file("file", "123".getBytes())
							.param("text", "text123")
							.param("tag", "tag123")
							.with(csrf()))
				.andDo(print())
				.andExpect(xpath("//div[@id='message-list']/div").nodeCount(5))
				.andExpect(xpath("//div[@id='message-list']/div/div[@data-id=10]").exists())
				.andExpect(xpath("//div[@id='message-list']/div/div[@data-id=10]/div/span").string("text123"))
				.andExpect(xpath("//div[@id='message-list']/div/div[@data-id=10]/div/i").string("tag123"));

	}
}
