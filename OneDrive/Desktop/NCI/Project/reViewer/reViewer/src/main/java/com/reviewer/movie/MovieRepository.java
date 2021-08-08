package com.reviewer.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, PagingAndSortingRepository<Movie, Long> {

//    @Query("SELECT m FROM Movie WHERE m.title LIKE %?1%")
//    public List<Movie> findAll(String keyword);

    @Query(value = "SELECT * FROM movie where "
            + "MATCH(title) "
            + "AGAINST (?1)",
            nativeQuery = true)
    public List<Movie> search(String keyword);

}
