package com.example.redditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditclone.dto.CommentsDto;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.mapper.CommentMapper;
import com.example.redditclone.model.Comment;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.User;
import com.example.redditclone.repository.CommentRepository;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CommentsService {
	
	private static final String POST_URL=""; 
	
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final CommentRepository commentRepository;
	private final CommentMapper commentMapper;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public void save(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(() -> new SpringRedditException("Post Not Found-"+commentsDto.getPostId()));
		Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
		commentRepository.save(comment);
		
		String message = mailContentBuilder.build(comment.getUser().getUsername()+" posted a comment on your post"+POST_URL);
		
		sendCommentNotification(comment.getUser().getUsername(), message,post.getUser());
	}

	private void sendCommentNotification(String currentUsername,String message, User user) {
		mailService.sendMail(new NotificationEmail(currentUsername+" commented on your post",user.getEmail(),message));
	}

	public List<CommentsDto> getAllCommentsForPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new SpringRedditException("post not found-"+postId));
		return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
	}

	public List<CommentsDto> getAllCommentsForUser(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found -"+username));
		return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
	}                 

	
}
