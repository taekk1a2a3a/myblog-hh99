package com.sparta.myblog.entity;

import com.sparta.myblog.dto.ReplyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private boolean deleted = false;

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
