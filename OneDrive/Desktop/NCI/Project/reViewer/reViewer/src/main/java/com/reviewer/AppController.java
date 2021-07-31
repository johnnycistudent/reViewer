package com.reviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AppController {
    @Autowired
    private MovieService service;

    @RequestMapping("/")
    public String viewHomePage(Model model){
        List<Movie> listMovies = service.listAll();
        model.addAttribute("listMovies", listMovies);

        return "index";
    }

    @RequestMapping("/new")
    public String showNewMovieForm(Model model){
        Movie movie = new Movie();
        model.addAttribute("movie", movie);

        return "new_movie";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveMovie(@ModelAttribute("movie") Movie movie){
        service.save(movie);

        return "redirect:/";

    }

    @RequestMapping("/edit/{movieID}")
    public ModelAndView showEditMovieForm(@PathVariable(name = "movieID") Long movieID){
        ModelAndView mav = new ModelAndView("edit_movie");

        Movie movie = service.get(movieID);
        mav.addObject("movie", movie);

        return mav;
    }

    @RequestMapping("/delete/{movieID}")
    public String deleteProduct(@PathVariable(name = "movieID") Long movieID){
        service.delete(movieID);

        return "redirect:/";
    }

}
