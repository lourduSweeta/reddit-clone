package com.example.redditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findAllBySubreddit(User user);
	List<Post> findByUser(User user);
	List<Post> findBySubreddit(Subreddit subreddit);
}
