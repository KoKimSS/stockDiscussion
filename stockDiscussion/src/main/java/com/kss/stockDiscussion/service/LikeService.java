package com.kss.stockDiscussion.service;

import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

//    @Autowired
//    private LikeRepository likeRepository;
//
//    public void likePost(User user, Poster poster) {
//        // 이미 좋아요를 눌렀는지 확인
//        if (!isPostLikedByUser(user, poster)) {
//            Like like = new Like();
//            like.setUser(user);
//            like.setPost(poster);
//            // 다른 필요한 정보들을 설정하고 저장
//            likeRepository.save(like);
//        }
//    }
//
//    public void unlikePost(User user, Poster poster) {
//        // 좋아요 취소
//        likeRepository.deleteByUserAndPost(user, poster);
//    }
//
//    public boolean isPostLikedByUser(User user, Poster poster) {
//        // 사용자가 해당 글에 좋아요를 이미 눌렀는지 확인
//        return likeRepository.existsByUserAndPost(user, poster);
//    }

}
