package com.andrey.speaker.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.andrey.speaker.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	User findUserByUsername(String username);
	User findUserByActivationCode(String code);
}
