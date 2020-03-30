package com.andrey.speaker.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.andrey.speaker.domain.Role;
import com.andrey.speaker.domain.User;
import com.andrey.speaker.persistence.UserRepository;

@Service
public class UserService implements UserDetailsService{

	private UserRepository usrRepo;
	private PasswordEncoder encoder;
	private MailSenderService mailService;
	
	@Autowired
	public UserService(UserRepository usrRepo,
			PasswordEncoder encoder,
			MailSenderService mailService) {
		this.usrRepo = usrRepo;
		this.encoder = encoder;
		this.mailService = mailService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usrRepo.findUserByUsername(username);
		if (user == null) throw new UsernameNotFoundException("No such user");
		
		return user;
	}
	
	public boolean addUser(User user) {
		User checkedUser = usrRepo.findUserByUsername(user.getUsername());
		if (checkedUser != null) {
			return false;
		}
		
		user.setPassword(encoder.encode(user.getPassword()));
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		user.setActivationCode(UUID.randomUUID().toString());
		
		sendActivationCode(user);
		
		usrRepo.save(user);
		
		return true;
	}
	
	public void sendActivationCode(User user) {
		if(!StringUtils.isEmpty(user.getMail())) {
			String text = String.format("Welcome to speaker dear %s!\n"
					+ 					" please confirm your email address with that link\n"
					+ 					" http://localhost:8080/register/activation/%s", user.getUsername(), user.getActivationCode());	
			mailService.send(user.getMail(), "Activation code", text);
		}
	}

	public boolean activateUser(String code) {
		User user = usrRepo.findUserByActivationCode(code);
		if (user != null) {
			user.setActivationCode(null);
			usrRepo.save(user);
			return true;
		}
		
		return false;
	}
	
	public void updateProfile(User user, String password, String mail) {
		boolean isMailChanged = false;
		String userMail = user.getMail();
		if (userMail != null) {
			isMailChanged = !userMail.equals(mail);
		}
		if (isMailChanged) {
			user.setMail(mail);
			if (!StringUtils.isEmpty(mail)) {
				user.setActivationCode(UUID.randomUUID().toString());
			}
		}
		
		if (!StringUtils.isEmpty(password)) {
			user.setPassword(encoder.encode(password));
		}
		
		usrRepo.save(user);
		
		if (isMailChanged) {
			sendActivationCode(user);
		}
	}

	public void editUserRoles(User user, String[] roles) {
		user.getRoles().clear();
		user.setRoles(Arrays.stream(roles).map((stringRole) -> Role.valueOf(stringRole)).collect(Collectors.toSet()));
		
		usrRepo.save(user);
		
	}

}
