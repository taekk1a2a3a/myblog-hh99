package com.sparta.myblog.repository;

import com.sparta.myblog.entity.Likes;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.Reply;
import com.sparta.myblog.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndPost(Users user, Post post);
    Optional<Likes> findByUserAndReply(Users user, Reply reply);

    @Query("SELECT l FROM Likes l WHERE l.post = :post AND l.deleted = false")
    List<Likes> findAllByPost(@Param("post") Post post);

    @Query(value = "select likes from Likes likes where likes.reply = ?1 and likes.deleted = false")
    List<Likes> findAllByReply(Reply reply);
}
