package com.reviewer.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
//    Page<Comment> findByReviewID(Long reviewID, Pageable pageable);
//    Optional<Comment> findByIdAndReviewId(Long id, Long reviewID);
}
