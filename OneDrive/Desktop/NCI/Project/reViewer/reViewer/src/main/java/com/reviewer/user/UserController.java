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

    // save movie
    @PostMapping ( "/save_favourite/{id}")
    public String saveFavourite(@PathVariable(name = "id") Long movieID,Model model,
                                @AuthenticationPrincipal CustomUserDetails currentUser)  {

        Long currentUserID = currentUser.getSessionUserID();
        User user = userRepo.findById(currentUserID).get();
        Movie movie = movieRepo.findById(movieID).get();
        model.addAttribute("movie", movie);
        //user.setFavourites(movie);
        user.getFavourites().add(movie);

        userRepo.save(user);

        return "redirect:/movies";

    }
}
