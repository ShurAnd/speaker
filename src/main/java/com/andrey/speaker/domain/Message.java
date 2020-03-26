package com.andrey.speaker.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Messages")
@Data
public class Message {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String message;
	private String tag;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User author;
	
	private String filename;
	
	public Message() {
		
	}
	
	public Message(String message, String tag, User author) {
		this.message = message;
		this.tag = tag;
		this.author = author;
	}
	
	public String getAuthorName() {
		return this.author != null  ? author.getUsername() : "<none>";
	}
}
