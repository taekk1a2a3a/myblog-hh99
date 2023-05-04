package com.sparta.myblog.repository;

import com.sparta.myblog.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);

    @Query("SELECT l FROM PostLike l WHERE l.post = :post AND l.deleted = false")
    List<PostLike> findAllByPost(@Param("post") Post post);


}
