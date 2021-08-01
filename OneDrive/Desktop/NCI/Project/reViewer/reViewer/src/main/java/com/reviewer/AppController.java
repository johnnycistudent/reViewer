package com.reviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AppController {
    @Autowired
    private UserRepository repo;
    private MovieService service;

    // home
    @GetMapping("/")
    public String viewHomePage(){
        return "index";
    }

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
        repo.save(user);

        return "registration_success";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model){
        List<User> listUsers = repo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    // Movie endpoints
    @GetMapping("/movies")
    public String viewMovies(Model model){
        List<Movie> listMovies = service.listAll();
        model.addAttribute("listMovies", listMovies);

        return "movies";
    }

    @RequestMapping("/new_movie")
    public String showNewMovieForm(Model model){
        Movie movie = new Movie();
        model.addAttribute("movie", movie);

        return "new_movie";
    }

    @RequestMapping(value = "/save_movie", method = RequestMethod.POST)
    public String saveMovie(@ModelAttribute("movie") Movie movie){
        service.save(movie);

        return "redirect:/movies";

    }

    @RequestMapping("/edit_movie/{movieID}")
    public ModelAndView showEditMovieForm(@PathVariable(name = "movieID") Long movieID){
        ModelAndView mav = new ModelAndView("edit_movie");

        Movie movie = service.get(movieID);
        mav.addObject("movie", movie);

        return mav;
    }

    @RequestMapping("/delete_movie/{movieID}")
    public String deleteProduct(@PathVariable(name = "movieID") Long movieID){
        service.delete(movieID);

        return "redirect:/movies";
    }

}
