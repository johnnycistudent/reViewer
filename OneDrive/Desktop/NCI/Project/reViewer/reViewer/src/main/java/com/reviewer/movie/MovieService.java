package com.reviewer.movie;

import com.reviewer.review.Review;
import com.reviewer.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    public Page<Movie> listPages(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        return movieRepo.findAll(pageable);
    }

    public void save(Movie movie){
        movieRepo.save(movie);
    }

    public Movie get(Long id){
        return movieRepo.findById(id).get();
    }

    public void delete(Long id){
        movieRepo.deleteById(id);
    }

    public List<Movie> search(String keyword){
        return movieRepo.searchMovies(keyword);
    }
}
