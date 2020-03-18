package com.andrey.speaker.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="usr")
@Data
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	private boolean active;
	
	@ElementCollection(targetClass=Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name="user_role", joinColumns=@JoinColumn(name="usr_id"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<>();
}
