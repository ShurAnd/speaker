package com.andrey.speaker.domain.dto;

import com.andrey.speaker.domain.Message;
import com.andrey.speaker.domain.User;

import lombok.Data;
import lombok.Getter;

@Getter
public class MessageDTO {

	private Long id;
	private String text;
	private String tag;
	private User author;
	private String filename;
	private Long likes;
	private boolean meLiked;
	
	
	public MessageDTO(Message message, long likes, boolean meLiked) {
		this.likes = likes;
		this.meLiked = meLiked;
		this.id = message.getId();
		this.text = message.getText();
		this.tag = message.getTag();
		this.author = message.getAuthor();
		this.filename = message.getFilename();
	}
	
	public String getAuthorName() {
		return author.getUsername() != null? author.getUsername() : "<none>";
	}
	
	
	@Override
	public String toString() {
		return ""+ id + " " + text + " " + likes + " " + meLiked;
	}
}
