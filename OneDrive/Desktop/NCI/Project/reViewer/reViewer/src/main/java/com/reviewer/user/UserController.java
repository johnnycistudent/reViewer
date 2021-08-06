package com.reviewer.user;

import com.reviewer.FileUploadUtil;
import com.reviewer.movie.Movie;
import com.reviewer.movie.MovieRepository;
import org.apache.catalina.security.SecurityClassLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MovieRepository movieRepo;

    // user endpoints
    @GetMapping("/register")
    public String showSignUpForm(Model model){
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_registration")
    public String processRegistration(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);

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

    @GetMapping("/list_users")
    public String viewUsersList(Model model){
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable(name = "id") Long id, Model model){
        User user = userRepo.findById(id).get();
        model.addAttribute("user", user);
        model.addAttribute("reviewList", user.getReviews());

        return "user";
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
                                @AuthenticationPrincipal CustomUserDetails currentUser)  {
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
        } else {
            user.getFavourites().add(movie);
        }

        userRepo.save(user);

        return "redirect:/movies";
    }

    // save movie to seen
    @PostMapping ( "/save_seen/{id}")
    public String saveSeen(@PathVariable(name = "id") Long movieID,
                                @AuthenticationPrincipal CustomUserDetails currentUser)  {
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
        } else {
            // else add movie to user's set of seen
            user.getSeen().add(movie);
        }

        userRepo.save(user);

        return "redirect:/movies";
    }

    // save movie to want-to-watch
    @PostMapping ( "/save_want/{id}")
    public String saveWant(@PathVariable(name = "id") Long movieID,
                           @AuthenticationPrincipal CustomUserDetails currentUser)  {
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
        } else {
            user.getWant().add(movie);
        }

        userRepo.save(user);

        return "redirect:/movies";
    }
}
