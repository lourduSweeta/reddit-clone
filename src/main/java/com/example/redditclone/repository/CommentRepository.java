package com.example.redditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.redditclone.model.Comment;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByPost(Post post);
	List<Comment> findAllByUser(User user);
	
}
