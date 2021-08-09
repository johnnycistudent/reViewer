package com.reviewer.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT rev FROM Review rev WHERE rev.movie.movieID = ?1")
    public List<Review> findAllByMovieID(Long id);
}
