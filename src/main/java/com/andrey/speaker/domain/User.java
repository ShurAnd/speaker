package com.andrey.speaker.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity
@Table(name="usr")
@Data
public class User implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotBlank(message="username cant be empty")
	private String username;
	@NotBlank(message="password cant be empty")
	private String password;
	private boolean active;
	@Email(message="email isnt correct")
	@NotBlank(message="email cant be empty")
	private String mail;
	private String activationCode;
	@OneToMany(mappedBy="author", cascade = CascadeType.ALL, fetch= FetchType.LAZY)
	private Set<Message> messages = new HashSet<>();
	
	@ManyToMany
	@JoinTable(
			name="subscribers",
			joinColumns= {@JoinColumn(name = "channel_id")},
			inverseJoinColumns= {@JoinColumn(name="sub_id")})
	private Set<User> subscribers = new HashSet<>();
	
	@ManyToMany
	@JoinTable(
			name="subscribers",
			joinColumns= {@JoinColumn(name = "sub_id")},
			inverseJoinColumns= {@JoinColumn(name="channel_id")})
	private Set<User> subscriptions = new HashSet<>();
	
	
	@ElementCollection(targetClass=Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name="user_role", joinColumns=@JoinColumn(name="user_id"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<>();
	
	public boolean isAdmin() {
		return roles.contains(Role.ADMIN);
	}
	
	public boolean isSubscriber(User user) {
		return subscribers.contains(user);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isActive();
	}
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (this == null || !(that instanceof User)) return false;
		
		User user2 = (User)that;
		return this.id.equals(user2.id) && this.username.equals(user2.username);
	}
	
	@Override
	public int hashCode() {
		int result = 0;
		result = 7 * (username.hashCode() + id.hashCode());
		
		return result;
	}
	
	
	@Override
	public String toString() {
		return username;
	}
}
