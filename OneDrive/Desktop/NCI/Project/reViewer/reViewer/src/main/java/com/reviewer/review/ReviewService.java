package com.reviewer.review;

import com.reviewer.movie.Movie;
import com.reviewer.movie.MovieRepository;
import com.reviewer.movie.MovieService;
import com.reviewer.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private MovieService movieService;

    public Page<Review> listReviewPages(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        return reviewRepo.findAll(pageable);
    }

    public void save(Review review){
        reviewRepo.save(review);
    }

    public Review get(Long id){
        return reviewRepo.findById(id).get();
    }

    public void delete(Long id){
        reviewRepo.deleteById(id);
    }

    // public List<Review> search(String keyword){
     //   return reviewRepo.searchReviews(keyword);
    //}

    public void calculateAverageRating(Review review){
        int userRating = review.getRating();
        System.out.println("User rating: " + userRating);

        Movie movie = review.getMovie();
        System.out.println("Movie: " + movie);
        Long movieID = movie.getMovieID();
        System.out.println("movieID: " + movieID);
        List<Review> reviewList = reviewRepo.findAllByMovieID(movieID);
        int movieCount = reviewList.size();
        System.out.println("Movie Count: " + movieCount);

        int currentAvg = movie.getAvgRating();
        System.out.println("currentAvg: " + currentAvg);
        int newAvgRating = Math.round((currentAvg + userRating)/ movieCount);
        System.out.println("newAvgRating: " + newAvgRating);
        movie.setAvgRating(newAvgRating);

        movieRepo.save(movie);
    }


}
