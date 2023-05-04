package com.sparta.myblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.myblog.dto.PostRequestDto;
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
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE post_id = ?")
@Where(clause = "deleted = false")
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Like> likeList = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likes;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @OrderBy("createdAt desc")
    private List<Reply> replyList = new ArrayList<>();

    @Column(nullable = false)
    @JsonIgnore
    private boolean deleted = false;

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void incLike() {
        ++likes;
    }
    public void decLike() {
        --likes;
    }
}