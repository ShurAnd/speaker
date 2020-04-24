package com.andrey.speaker.ut;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andrey.speaker.domain.Role;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.persistence.UserRepository;
import com.andrey.speaker.service.MailSenderService;
import com.andrey.speaker.service.UserService;

public class UserServiceTests {

	private UserRepository usrRepo = Mockito.mock(UserRepository.class);
	private PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
	private MailSenderService mailService = Mockito.mock(MailSenderService.class);
	private UserService userService;
	
	@BeforeEach
	public void init() {
		userService = new UserService(usrRepo, encoder, mailService);
	}
	
	@Test
	public void shouldReturnUserServiceWhenLoadByUsername() {
		User user = new User();
		user.setUsername("Andrey");
		doReturn(user).when(usrRepo).findUserByUsername(user.getUsername());
		UserDetails userDetails = userService.loadUserByUsername("Andrey");
		
		assertThat(userDetails).isEqualTo(user);
	}
	
	@Test
	public void shouldFailLoadUserbyUsername() {
		User user = new User();
		user.setUsername("Andrey");
		doReturn(null).when(usrRepo).findUserByUsername(user.getUsername());
		
		org.junit.jupiter.api.Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("Andrey"));
	}
	
	@Test
	public void shouldAddUser() {
		User user = new User();
		user.setUsername("user1");
		user.setMail("mail@mail");
		boolean added = userService.addUser(user);
		org.assertj.core.api.Assertions.assertThat(added).isTrue();
		assertThat(user.getActivationCode()).isNotNull();
		assertThat(user.isActive()).isTrue();
		assertThat(user.getRoles()).isEqualTo(Collections.singleton(Role.USER));
		
		Mockito.verify(usrRepo, Mockito.times(1)).save(user);
		Mockito.verify(mailService, Mockito.times(1)).send(ArgumentMatchers.eq(user.getMail()), ArgumentMatchers.eq("Activation code"), ArgumentMatchers.contains("Welcome to speaker dear"));
		
	}
	
	@Test
	public void shouldFailAddingUser() {
		User user = new User();
		user.setUsername("Andrey");
		doReturn(user).when(usrRepo).findUserByUsername(user.getUsername());
		boolean added = userService.addUser(user);
		
		assertThat(added).isFalse();
	}
	
	@Test
	public void shouldSendActivationCode() {
		User user = new User();
		user.setUsername("Andrey");
		user.setMail("mail@mail");
		userService.sendActivationCode(user);
		
		Mockito.verify(mailService, Mockito.times(1)).
			send(ArgumentMatchers.eq(user.getMail()),
				ArgumentMatchers.eq("Activation code"),
				ArgumentMatchers.contains("Welcome to speaker dear " + user.getUsername()));
	}
	
	@Test
	public void shouldActivateUser() {
		User user = new User();
		user.setActivationCode("123");
		
		doReturn(user).when(usrRepo).findUserByActivationCode(user.getActivationCode());
		
		boolean activated = userService.activateUser(user.getActivationCode());
		
		assertThat(activated).isTrue();
		assertThat(user.getActivationCode()).isNull();
	}
	
	@Test
	public void shouldFailActivationUser() {
		User user = new User();
		user.setActivationCode("123");
		
		doReturn(null).when(usrRepo).findUserByActivationCode(user.getActivationCode());
		boolean activated = userService.activateUser(user.getActivationCode());
		
		assertThat(activated).isFalse();
		assertThat(user.getActivationCode()).isNotNull();
	}
	
	@Test
	public void shouldUpdateProfile() {
		User user = new User();
		user.setUsername("Andrey");
		user.setMail("mail@mail");
		user.setPassword("123");
		
		userService.updateProfile(user, user.getPassword(), "newMail@mail");
		
		assertThat(user.getMail()).isEqualTo("newMail@mail");
		assertThat(user.getActivationCode()).isNotNull();
		Mockito.verify(encoder, Mockito.times(1)).encode("123");
		Mockito.verify(usrRepo, Mockito.times(1)).save(user);
		Mockito.verify(mailService, Mockito.times(1)).send(ArgumentMatchers.eq("newMail@mail"), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
	}
	
	@Test
	public void shouldEditUserRoles() {
		User user = new User();
		user.setRoles(new HashSet<>(Arrays.asList(Role.USER)));
		
		userService.editUserRoles(user, new String[] {"USER", "ADMIN"});
		
		assertThat(user.getRoles()).isEqualTo(new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)));
		Mockito.verify(usrRepo, Mockito.times(1)).save(user);
	}
	
	@Test
	public void shouldSubscribeAndUnscribe() {
		User user = new User();
		user.setId(1L);
		user.setUsername("User");
		User subscriber = new User();
		subscriber.setId(2L);
		subscriber.setUsername("Subscriber");
		
		userService.subscribe(user, subscriber);
		assertThat(user.getSubscribers()).contains(subscriber);
		Mockito.verify(usrRepo, Mockito.times(1)).save(user);
		
		userService.unscribe(user, subscriber);
		assertThat(user.getSubscribers()).doesNotContain(subscriber);
		Mockito.verify(usrRepo, Mockito.times(2)).save(user);
	}
}
