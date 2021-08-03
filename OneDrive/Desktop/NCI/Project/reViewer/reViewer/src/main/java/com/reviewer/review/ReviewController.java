package com.reviewer.review;

import com.reviewer.movie.Movie;
import com.reviewer.movie.MovieRepository;
import com.reviewer.user.User;
import com.reviewer.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private MovieRepository movieRepo;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/reviews")
    public String listReviews(Model model){
        List<Review> listReviews = reviewRepo.findAll();
        model.addAttribute("listReviews", listReviews);

        return "reviews";
    }

    @GetMapping("/new_review")
    public String showNewReviewForm(Model model){
        List<Movie> listMovies = movieRepo.findAll();
        List<User> listUsers = userRepo.findAll();
        Review review = new Review();

        model.addAttribute("review", review);
        model.addAttribute("listMovies", listMovies);
        model.addAttribute("listUsers", listUsers);

        return "review_form";
    }

    @PostMapping(value = "/save_review")
    public String saveReview(Review review){
        reviewRepo.save(review);

        return "redirect:/reviews";
    }

    @GetMapping("/edit_review/{id}")
    public String showEditReviewForm(@PathVariable(name = "id") Long id, Model model){

        Review review = reviewRepo.findById(id).get();
        model.addAttribute("review", review);

        List<Movie> listMovies = movieRepo.findAll();
        model.addAttribute("listMovies", listMovies);

        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "review_form";
    }

    @GetMapping("/delete_review/{id}")
    public String deleteReview(@PathVariable(name = "id") Long id, Model model){
        reviewRepo.deleteById(id);

        return "redirect:/reviews";
    }

}
