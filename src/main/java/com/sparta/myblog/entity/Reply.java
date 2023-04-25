package com.sparta.myblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.myblog.dto.ReplyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Reply extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;
    @Column(nullable = false)
    private String contents;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Reply(ReplyRequestDto requestDto, Users user, Post post) {
        this.contents = requestDto.getContents();
        this.user = user;
        this.post = post;
    }

    public void update(ReplyRequestDto requestDto, Users user){
        this.contents = requestDto.getContents();
        this.user = user;
    }
}
