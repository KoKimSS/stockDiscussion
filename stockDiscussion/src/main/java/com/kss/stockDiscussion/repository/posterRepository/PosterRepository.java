package com.kss.stockDiscussion.repository.posterRepository;

import com.kss.stockDiscussion.domain.poster.Poster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosterRepository extends JpaRepository<Poster,Long> {
}
