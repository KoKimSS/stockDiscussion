package com.kss.stockDiscussion.domain.user;

import com.kss.stockDiscussion.domain.baseEntity.BaseEntity;
import com.kss.stockDiscussion.domain.follow.Follow;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String loginId;

    private String password;
    private String name;
    private String email;
    private String introduction;
    private String img_path;

    @OneToMany(mappedBy = "following")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> following = new ArrayList<>();
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
