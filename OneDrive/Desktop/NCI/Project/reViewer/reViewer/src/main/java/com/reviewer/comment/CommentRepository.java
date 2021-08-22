package com.reviewer.comment;

import com.reviewer.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("DELETE FROM Comment comment WHERE comment.review.movie.movieID = ?1")
    public List<Comment> deleteAllCommentsByMovieID(Long id);
}
