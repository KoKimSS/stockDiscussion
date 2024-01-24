package com.kss.stockDiscussion.domain.user;

import com.kss.stockDiscussion.domain.baseEntity.BaseEntity;
import com.kss.stockDiscussion.domain.follow.Follow;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String name;
    private String introduction;
    private String img_path;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToMany(mappedBy = "following")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> following = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String introduction, String img_path, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.introduction = introduction;
        this.img_path = img_path;
        this.role = role;
    }
}
