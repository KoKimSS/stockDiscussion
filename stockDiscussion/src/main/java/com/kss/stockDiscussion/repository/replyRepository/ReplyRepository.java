package com.kss.stockDiscussion.repository.replyRepository;

import com.kss.stockDiscussion.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {
}
