package com.sparta.myblog.repository;

import com.sparta.myblog.entity.Like;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.Reply;
import com.sparta.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndReply(User user, Reply reply);

    @Query("SELECT l FROM Like l WHERE l.post = :post AND l.deleted = false")
    List<Like> findAllByPost(@Param("post") Post post);

    @Query(value = "select likes from Like likes where likes.reply = ?1 and likes.deleted = false")
    List<Like> findAllByReply(Reply reply);
}
