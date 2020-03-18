package com.andrey.speaker.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(17);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/register").permitAll()
			.anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login")
			.permitAll()
		.and()
			.logout().logoutUrl("/logout")
			.permitAll();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(encoder())
			.usersByUsernameQuery("select username, password, active from usr where username=?")
			.authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id=ur.usr_id where u.username=?");
	}
}
