package com.example.redditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.redditclone.dto.PostRequest;
import com.example.redditclone.dto.PostResponse;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.exception.SubredditNotFoundException;
import com.example.redditclone.mapper.PostMapper;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.model.User;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.SubredditRepository;
import com.example.redditclone.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class PostService {
	
	private final PostRepository postRepository;
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final PostMapper postMapper;
	private final UserRepository userRepository;
	
	public void save(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
				.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
	}

	public List<PostResponse> getAllPosts() {
		return postRepository.findAll()
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}

	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new SpringRedditException("Post not found-"+id));
		return postMapper.mapToDto(post);
	}

	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new SpringRedditException("Subreddit not found-"+subredditId));
		 List<Post> posts = postRepository.findBySubreddit(subreddit);
		return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}

	public List<PostResponse> getPostsByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new SpringRedditException("User name not found - "+username));
		
		return postRepository.findByUser(user).stream().map(postMapper::mapToDto)
		.collect(Collectors.toList());
		
	}
	

}
