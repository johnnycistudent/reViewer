package com.reviewer.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByReviewID(Long reviewID, Pageable pageable);
    Optional<Comment> findByIdAndPostId(Long id, Long reviewID);
}
