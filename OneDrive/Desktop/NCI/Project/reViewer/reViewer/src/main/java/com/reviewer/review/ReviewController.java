package com.reviewer.review;

import com.reviewer.movie.Movie;
import com.reviewer.movie.MovieRepository;
import com.reviewer.user.CustomUserDetails;
import com.reviewer.user.User;
import com.reviewer.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/review/{id}")
    public String showReview(@PathVariable(name = "id") Long id, Model model){
        Review review = reviewRepo.findById(id).get();

        model.addAttribute("review", review);
        //model.addAttribute("reviewList", review.getReviews());

        return "review";
    }

    @GetMapping("/new_review/{id}")
    public String showNewReviewForm(@PathVariable(name = "id") Long id, Model model){

        Movie movie = movieRepo.findById(id).get();
        Review review = new Review();

        model.addAttribute("review", review);
        model.addAttribute("movie", movie);
        //model.addAttribute("currentUser", currentUser);

        return "review_form";
    }

    @PostMapping(value = "/save_review/{id}")
    public String saveReview(@PathVariable(name = "id") Long movieID,
                             Review review,
                             @AuthenticationPrincipal CustomUserDetails currentUser){
        Long currentUserID = currentUser.getSessionUserID();
        Movie movie = movieRepo.findById(movieID).get();
        User user = userRepo.findById(currentUserID).get();
        review.setUser(user);
        review.setMovie(movie);

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
