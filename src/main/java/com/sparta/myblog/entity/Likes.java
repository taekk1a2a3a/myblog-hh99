package com.sparta.myblog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "likes")
@SQLDelete(sql = "UPDATE likes SET deleted = true WHERE likes_id = ?")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    @JsonBackReference
    private Reply reply;

    @Column(nullable = false)
    @JsonIgnore
    private boolean deleted = false;

    public Likes(Users user, Post post) {
        this.user = user;
        this.post = post;
    }
    public Likes(Users user, Reply reply){
        this.user = user;
        this.reply = reply;
    }
}
