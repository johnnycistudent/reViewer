package com.reviewer.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %?1%")
    public List<Movie> searchMovies(String keyword);


}
