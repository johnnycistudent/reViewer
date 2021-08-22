package com.reviewer.user;

import com.reviewer.FileUploadUtil;
import com.reviewer.comment.Comment;
import com.reviewer.comment.CommentRepository;
import com.reviewer.movie.Movie;
import com.reviewer.movie.MovieRepository;
import com.reviewer.review.Review;
import com.reviewer.review.ReviewRepository;
import org.apache.catalina.security.SecurityClassLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CustomDetailsService userService;
    @Autowired
    private MovieRepository movieRepo;
    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private CommentRepository commentRepo;

    // user endpoints
    @GetMapping("/register")
    public String showSignUpForm(Model model){
        // add model for page title + user to form for data capture
        model.addAttribute("pageTitle", "Register New User");
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_registration")
    public String processRegistration(User user, Model model) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());

        if(userRepo.findByUserName(user.getUserName()) != null){
            String message = "Username " + user.getUserName() + " already exists. Please choose another username";
            model.addAttribute("message", message);
            return "signup_form";
        } else if(userRepo.findByEmailAddress(user.getEmailAddress()) != null) {
            String message = "The email address - " + user.getEmailAddress()
                    + " - already exists. Please choose another email or login";
            model.addAttribute("message", message);
            return "signup_form";
        } else  {
            user.setPassword(encodedPassword);
            userRepo.save(user);
        }

        return "registration_success";
    }

    // login
    @GetMapping("/login")
    public String showLoginPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }
        return "redirect:/";
    }

    // list all users
    @GetMapping("/list_users")
    public String listUsers(Model model){
        // find all users
//        List<User> listUsers = userRepo.findAll();
//
//        // add model for page title, user list
//        model.addAttribute("pageTitle", "Users List");
//        model.addAttribute("listUsers", listUsers);

        return listUsersByPage(model, 1);
    }

    // display users with pagination
    @GetMapping("/users/{pageNumber}")
    public String listUsersByPage(Model model, @PathVariable("pageNumber") int currentPage){
        // list all users with pagination using Page object
        Page<User> page = userService.listUsersPages(currentPage);
        // initiate total pages + no. of elements
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        // list the users in the database
        List<User> listUsers = page.getContent();

        // model attributes for page title, movies list etc
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listUsers", listUsers);

        // return users page
        return "users";
    }

    // single user view
    @GetMapping("/user/{id}")
    public String showUser(@PathVariable(name = "id") Long id, Model model){
        // find user by id passed through parameter
        User user = userRepo.findById(id).get();

        // add model for page title, user object, list reviews, favourites, seen, watchlist
        model.addAttribute("pageTitle", user.getUserName() + "'s Profile");
        model.addAttribute("user", user);
        model.addAttribute("reviewList", user.getReviews().stream()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
        model.addAttribute("favouriteList", user.getFavourites());
        model.addAttribute("seenList", user.getSeen());
        model.addAttribute("watchList", user.getWant());
        model.addAttribute("commentList", user.getComments().stream()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
        model.addAttribute("likedReviewList", user.getReviewLikes().stream()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
        model.addAttribute("likedCommentList", user.getCommentLikes().stream()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList()));

        return "user";
    }

    // search movies
    @GetMapping("/searchUsers")
    public String searchUsers(@Param("keyword") String keyword, Model model){

        // search users table using keyword parameter
        List<User> listUsers = userRepo.searchUsers(keyword);

        // add page title, user list and keyword to model
        model.addAttribute("pageTitle", "Search Users");
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("keyword", keyword);

        // return search results
        return "search_results";
    }

    @GetMapping("/user_profile")
    public String viewUserProfile(@AuthenticationPrincipal CustomUserDetails currentUser,
                                  Model model){
        model.addAttribute("currentUser", currentUser);

        return "user_profile";
    }

    // save movie to favourites
    @PostMapping ( "/save_favourite/{id}")
    public String saveFavourite(@PathVariable(name = "id") Long movieID,
                                @AuthenticationPrincipal CustomUserDetails currentUser,
                                RedirectAttributes userAlert)  {
        // Get current userID
        Long currentUserID = currentUser.getSessionUserID();
        // find current user in user repo by ID
        User user = userRepo.findById(currentUserID).get();
        // find movie's ID by argument passed in through front end
        Movie movie = movieRepo.findById(movieID).get();
        //model.addAttribute("movie", movie);

        if(user.getFavourites().contains(movie)) {
            // add movie to user's set of favourites
            user.getFavourites().remove(movie);
            userAlert.addFlashAttribute("success", movie.getTitle() + " was removed from your favourites!");
        } else {
            user.getFavourites().add(movie);
            userAlert.addFlashAttribute("success", movie.getTitle() + " was added to your favourites!");
        }

        userRepo.save(user);

        return "redirect:/movie/" + movieID;
    }

    // save movie to seen
    @PostMapping ( "/save_seen/{id}")
    public String saveSeen(@PathVariable(name = "id") Long movieID,
                           @AuthenticationPrincipal CustomUserDetails currentUser,
                           RedirectAttributes userAlert)  {
        // Get current userID
        Long currentUserID = currentUser.getSessionUserID();
        // find current user in user repo by ID
        User user = userRepo.findById(currentUserID).get();
        // find movie's ID by argument passed in through front end
        Movie movie = movieRepo.findById(movieID).get();
        //model.addAttribute("movie", movie);

        if(user.getSeen().contains(movie)) {
            // if the movie is on the user's set list, remove it
            user.getSeen().remove(movie);
            userAlert.addFlashAttribute("success", movie.getTitle() + " was removed from your Seen List!");
        } else {
            // else add movie to user's set of seen
            user.getSeen().add(movie);
            userAlert.addFlashAttribute("success", movie.getTitle() + " was added to your Seen List!");
        }

        userRepo.save(user);

        return "redirect:/movie/" + movieID;
    }

    // save movie to want-to-watch
    @PostMapping ( "/save_want/{id}")
    public String saveWant(@PathVariable(name = "id") Long movieID,
                           @AuthenticationPrincipal CustomUserDetails currentUser,
                           RedirectAttributes userAlert)  {
        // Get current userID
        Long currentUserID = currentUser.getSessionUserID();
        // find current user in user repo by ID
        User user = userRepo.findById(currentUserID).get();
        // find movie's ID by argument passed in through front end
        Movie movie = movieRepo.findById(movieID).get();
        //model.addAttribute("movie", movie);

        if(user.getWant().contains(movie)) {
            // add movie to user's set of favourites
            user.getWant().remove(movie);
            userAlert.addFlashAttribute("success", movie.getTitle() + " was removed from your Watchlist!");
        } else {
            user.getWant().add(movie);
            userAlert.addFlashAttribute("success", movie.getTitle() + " was added to your Watchlist!");
        }

        userRepo.save(user);

        return "redirect:/movie/" + movieID;
    }

    // like a review
    @PostMapping ( "/like_review/{id}")
    public String likeReview(@PathVariable(name = "id") Long reviewID,
                           @AuthenticationPrincipal CustomUserDetails currentUser,
                           RedirectAttributes userAlert)  {
        // Get current userID
        Long currentUserID = currentUser.getSessionUserID();
        // find current user in user repo by ID
        User user = userRepo.findById(currentUserID).get();
        // find review ID by argument passed in through front end
        Review review = reviewRepo.findById(reviewID).get();

        // if the user has already liked this review,
        if(user.getReviewLikes().contains(review)) {
            // remove it from the user's liked review list
            user.getReviewLikes().remove(review);
            // notify user
            userAlert.addFlashAttribute("success", review.getUser().getUserName() + "'s review was removed from your liked reviews!");
        } else {
            // if review isn't on the user's liked review list,
            // add it to their list
            user.getReviewLikes().add(review);
            // notify user
            userAlert.addFlashAttribute("success", review.getUser().getUserName() + "'s review was added to your liked reviews!");
        }

        userRepo.save(user);

        return "redirect:/review/" + reviewID;
    }

    // like a comment
    @PostMapping ( "/like_comment/{id}")
    public String likeComment(@PathVariable(name = "id") Long commentID,
                             @AuthenticationPrincipal CustomUserDetails currentUser,
                             RedirectAttributes userAlert)  {
        // Get current userID
        Long currentUserID = currentUser.getSessionUserID();
        // find current user in user repo by ID
        User user = userRepo.findById(currentUserID).get();
        // find comment ID by argument passed in through front end
        Comment comment = commentRepo.findById(commentID).get();

        // if the user has already liked this comment,
        if(user.getCommentLikes().contains(comment)) {
            // remove it from the user's liked comment list
            user.getCommentLikes().remove(comment);
            // notify user
            userAlert.addFlashAttribute("success", comment.getUser().getUserName() + "'s comment was removed from your liked comments!");
        } else {
            // if comment isn't on the user's liked comment list,
            // add it to their list
            user.getCommentLikes().add(comment);
            // notify user
            userAlert.addFlashAttribute("success", comment.getUser().getUserName() + "'s comment was added to your liked comments!");
        }

        userRepo.save(user);
        Long reviewID = comment.getReview().getReviewID();

        return "redirect:/review/" + reviewID;
    }
}
