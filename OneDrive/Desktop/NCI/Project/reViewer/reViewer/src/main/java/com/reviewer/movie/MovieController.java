package com.reviewer.movie;

import com.reviewer.review.Review;
import com.reviewer.user.User;
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

    @Autowired
    private MovieRepository movieRepo;

    // display all movies
    @GetMapping("/movies")
    public String listMovies(Model model){
        List<Movie> listMovies = service.listAll();
        model.addAttribute("listMovies", listMovies);

        return "movies";
    }

    @GetMapping("/movie/{id}")
    public String showMovie(@PathVariable(name = "id") Long id, Model model){
        Movie movie = movieRepo.findById(id).get();
        model.addAttribute("movie", movie);

        return "movie";
    }

    // add new movie
    @GetMapping("/new_movie")
    public String showNewMovieForm(Model model){
        Movie movie = new Movie();
        model.addAttribute("movie", movie);

        return "new_movie";
    }

    // save movie
    @RequestMapping(value = "/save_movie", method = RequestMethod.POST)
    public String saveMovie(@ModelAttribute("movie") Movie movie){
        service.save(movie);

        return "redirect:/movies";

    }

    // edit movie
    @RequestMapping("/edit_movie/{movieID}")
    public ModelAndView showEditMovieForm(@PathVariable(name = "movieID") Long movieID){
        ModelAndView mav = new ModelAndView("edit_movie");

        Movie movie = service.get(movieID);
        mav.addObject("movie", movie);

        return mav;
    }

    // delete movie
    @RequestMapping("/delete_movie/{movieID}")
    public String deleteProduct(@PathVariable(name = "movieID") Long movieID){
        service.delete(movieID);

        return "redirect:/movies";
    }
}
