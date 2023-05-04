package com.sparta.myblog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.myblog.dto.ReplyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "reply")
@SQLDelete(sql = "UPDATE reply SET deleted = true WHERE reply_id = ?")
@Where(clause = "deleted = false")
public class Reply extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    @OneToMany(mappedBy = "reply", orphanRemoval = true)
    @JsonIgnore
    private List<ReplyLike> likeList = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likes;

    @Column(nullable = false)
    @JsonIgnore
    private boolean deleted = false;

    public Reply(ReplyRequestDto requestDto, User user, Post post) {
        this.contents = requestDto.getContents();
        this.user = user;
        this.post = post;
    }

    public void update(ReplyRequestDto requestDto, User user){
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void incLike() {
        ++likes;
    }
    public void decLike() {
        --likes;
    }
}
