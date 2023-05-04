package com.sparta.myblog.repository;

import com.sparta.myblog.entity.Reply;
import com.sparta.myblog.entity.ReplyLike;
import com.sparta.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    Optional<ReplyLike> findByUserAndReply(User user, Reply reply);
    @Query(value = "select replylike from ReplyLike replylike where replylike.reply = ?1 and replylike.deleted = false")
    List<ReplyLike> findAllByReply(Reply reply);
}
