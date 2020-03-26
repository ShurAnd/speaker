package com.andrey.speaker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

	private JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String fromSend;
	
	public MailSenderService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void send(String sendTo, String subject, String message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(fromSend);
		mailMessage.setTo(sendTo);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		
		mailSender.send(mailMessage);
	}
}
