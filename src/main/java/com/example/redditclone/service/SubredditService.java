package com.example.redditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.mapper.SubredditMapper;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
	
	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto save(SubredditDto subreddit) {
		Subreddit savedReddit = subredditRepository.save(subredditMapper.mapDtoToSubrerddit(subreddit));
		subreddit.setId(savedReddit.getId());
		return subreddit;
	}
	
	@Transactional
	public List<SubredditDto> getAll() {
		
		return subredditRepository.findAll()
		.stream()
		.map(subredditMapper :: mapSubredditToDto)
		.collect(Collectors.toList());
	}

	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit =  subredditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("Subreddit not found for given id"));
		return subredditMapper.mapSubredditToDto(subreddit);
		
	}
	
	
}
