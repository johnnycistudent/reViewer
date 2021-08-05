package com.reviewer.review;

import com.reviewer.movie.Movie;
import com.reviewer.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repo;

    public void customSave(Review review){
        repo.save(review);
    }
}
