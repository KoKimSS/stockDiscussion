package com.kss.stockDiscussion.repository.likeRepository;


import com.kss.stockDiscussion.domain.like.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {

}
