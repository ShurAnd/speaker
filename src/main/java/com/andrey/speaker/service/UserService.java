package com.andrey.speaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andrey.speaker.persistence.UserRepository;

@Service
public class UserService implements UserDetailsService{

	private UserRepository usrRepo;
	
	@Autowired
	public UserService(UserRepository usrRepo) {
		this.usrRepo = usrRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return usrRepo.findUserByUsername(username);
	}

}
