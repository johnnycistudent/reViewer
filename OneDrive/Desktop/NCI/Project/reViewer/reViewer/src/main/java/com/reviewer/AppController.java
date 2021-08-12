package com.reviewer;

import com.reviewer.movie.Movie;
import com.reviewer.movie.MovieService;
import com.reviewer.user.User;
import com.reviewer.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AppController {

    // home
    @GetMapping("/")
    public String viewHomePage(Model model){
        // add model for page title
        model.addAttribute("pageTitle", "re:Viewer Homepage");
        return "index";
    }

    @GetMapping("/403")
    public String error403(Model model) {
        model.addAttribute("pageTitle", "403 error");
        return "403";
    }

}
