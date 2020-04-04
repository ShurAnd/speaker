package com.andrey.speaker.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;


import lombok.Data;

@Entity
@Table(name="Messages")
@Data
public class Message {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotBlank(message="Pls enter your message")
	@Length(max=2048, message = "max length is 2048")
	private String text;
	@Length(max=255)
	private String tag;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User author;
	
	private String filename;
	
	public Message() {
		
	}
	
	public Message(String message, String tag) {
		this.text = message;
		this.tag = tag;
	}
	
	public Message(String message, String tag, User author) {
		this.text = message;
		this.tag = tag;
		this.author = author;
	}
	
	public String getAuthorName() {
		return this.author != null  ? author.getUsername() : "<none>";
	}
	
	@PreRemove
	private void removeMessage() {
		author.getMessages().remove(this);
	}
}
