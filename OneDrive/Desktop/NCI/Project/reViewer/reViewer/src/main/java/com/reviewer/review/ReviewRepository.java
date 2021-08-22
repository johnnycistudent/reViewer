package com.reviewer.review;

import com.reviewer.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Review rev WHERE rev.movie.movieID = ?1")
    void deleteAllReviewsByMovieID(Long id);

    @Query("SELECT rev FROM Review rev WHERE rev.movie.movieID = ?1")
    public List<Review> findAllByMovieID(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment comment WHERE comment.review.movie.movieID = ?1")
    void deleteAllCommentsByMovieID(Long id);
}
