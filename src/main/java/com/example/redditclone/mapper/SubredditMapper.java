package com.example.redditclone.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
	
	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
	SubredditDto mapSubredditToDto(Subreddit subreddit);
	
	@InheritInverseConfiguration
	@Mapping(target = "posts", ignore=true)
	Subreddit mapDtoToSubrerddit(SubredditDto subredditDto);
	
	default Integer mapPosts(List<Post> posts) {
		return posts.size();
	}
}
