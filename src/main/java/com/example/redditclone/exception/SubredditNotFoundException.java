package com.example.redditclone.exception;

public class SubredditNotFoundException extends RuntimeException{
	public SubredditNotFoundException(String message) {
		super(message);
	}
}
