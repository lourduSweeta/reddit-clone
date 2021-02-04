package com.example.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponse {

	private Long id;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String subredditName;
}
