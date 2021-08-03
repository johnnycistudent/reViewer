package com.reviewer.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping("/movies")
    public String listMovies(Model model){
        List<Movie> listMovies = service.listAll();
        model.addAttribute("listMovies", listMovies);

        return "movies";
    }

    @GetMapping("/new_movie")
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
