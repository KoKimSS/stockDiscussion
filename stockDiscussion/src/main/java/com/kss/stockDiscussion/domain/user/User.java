package com.kss.stockDiscussion.domain.user;

import com.kss.stockDiscussion.domain.baseEntity.BaseEntity;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.like.Likes;
import com.kss.stockDiscussion.domain.reply.Reply;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name","email","introduction","img_path" })
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String name;
    private String introduction;
    private String imgPath;

    private String roles; //USER, ADMIN
    @OneToMany(mappedBy = "following")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> following = new ArrayList<>();
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Likes> likesList = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String introduction, String imgPath) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.introduction = introduction;
        this.imgPath = imgPath;
        this.roles = "USER";
    }
    public List<String> getRoleList() {
        if (!this.roles.equals(null)) {
            return Arrays.asList(this.roles.split(" "));
        }
        return new ArrayList<>();
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateProfile(String name ,String imgPath, String introduction){
        this.name=name;
        this.imgPath =imgPath;
        this.introduction=introduction;
    }
}
