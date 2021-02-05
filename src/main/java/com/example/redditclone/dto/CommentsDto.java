package com.example.redditclone.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
	private Long id;
	private Long postId;
	private String text;
	private String username;
	private Instant createdDate;
}
