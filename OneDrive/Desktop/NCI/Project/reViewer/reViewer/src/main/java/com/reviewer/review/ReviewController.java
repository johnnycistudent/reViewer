package com.reviewer.review;

import com.reviewer.comment.Comment;
import com.reviewer.movie.Movie;
import com.reviewer.movie.MovieRepository;
import com.reviewer.movie.MovieService;
import com.reviewer.user.CustomUserDetails;
import com.reviewer.user.User;
import com.reviewer.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MovieRepository movieRepo;
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/reviews")
    public String listReviews(Model model){
        List<Review> listReviews = reviewRepo.findAll();
        model.addAttribute("listReviews", listReviews);

        return listReviewsByPage(model, 1);
    }

    // display reviews with pagination
    @GetMapping("/reviews/{pageNumber}")
    public String listReviewsByPage(Model model, @PathVariable("pageNumber") int currentPage){
        // list all reviews with pagination using Page object
        Page<Review> page = reviewService.listReviewPages(currentPage);
        // initiate total pages + no. of elements
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        // list the movies in the database
        List<Review> listReviews = page.getContent();

        // model attributes for page title, movies list etc
        model.addAttribute("pageTitle", "Reviews");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listReviews", listReviews);

        // return movies page
        return "reviews";
    }

    @GetMapping("/review/{id}")
    public String showReview(@PathVariable(name = "id") Long id, Model model){
        Review review = reviewRepo.findById(id).get();

        Comment newComment = new Comment();

        System.out.println(review.getComments());

        Set commentList = review.getComments();

        model.addAttribute("pageTitle", review.getMovie().getTitle() + "Review");
        model.addAttribute("review", review);
        model.addAttribute("commentList", commentList);
        model.addAttribute("newComment", newComment);

        return "review";
    }

    @GetMapping("/new_review/{id}")
    public String showNewReviewForm(@PathVariable(name = "id") Long id, Model model){

        // find movie by id passed through the method parameter
        Movie movie = movieRepo.findById(id).get();
        // initialize new Review object
        Review review = new Review();

        // add pageTitle, review and movie object to page
        model.addAttribute("pageTitle", "New Review");
        model.addAttribute("review", review);
        model.addAttribute("movie", movie);

        return "review_form";
    }

    @PostMapping(value = "/save_review/{id}")
    public String saveReview(@PathVariable(name = "id") Long movieID,
                             Review review,
                             // captures session user info
                             @AuthenticationPrincipal CustomUserDetails currentUser){
        // current userID
        Long currentUserID = currentUser.getSessionUserID();
        // current movie
        Movie movie = movieRepo.findById(movieID).get();
        // get current user in the database
        User user = userRepo.findById(currentUserID).get();
        // set the user as the author of the review
        review.setUser(user);
        // set the movie as the movie being reviewed
        review.setMovie(movie);

        // save the review
        reviewRepo.save(review);
        // calculate the new average rating
        reviewService.calculateAverageRating(review);


        //return "redirect:/reviews";
        return "redirect:/review/" + review.getReviewID();
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
