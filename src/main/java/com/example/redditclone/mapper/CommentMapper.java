package com.example.redditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.redditclone.dto.CommentsDto;
import com.example.redditclone.model.Comment;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper 
{
	@Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
	@Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
	CommentsDto mapToDto(Comment comment);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "text" , source = "commentsDto.text")
	@Mapping(target = "post", source = "post")
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "user" , source = "user")
	Comment map(CommentsDto commentsDto,Post post, User user);
}
