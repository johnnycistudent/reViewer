package com.reviewer.movie;

import com.reviewer.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepo;

    // display all movies
    @GetMapping("/movies")
    public String listMovies(Model model){


        //List<Movie> listMovies = movieRepo.findAll();
        //model.addAttribute("listMovies", listMovies);

        return listMoviesByPage(model, 1);
    }

    // display movies with pagination
    @GetMapping("/movies/{pageNumber}")
    public String listMoviesByPage(Model model, @PathVariable("pageNumber") int currentPage){
        // list all movies with pagination using Page object
        Page<Movie> page = movieService.listPages(currentPage);
        // initiate total pages + no. of elements
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        // list the movies in the database
        List<Movie> listMovies = page.getContent();

        // model attributes for page title, movies list etc
        model.addAttribute("pageTitle", "Movies");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listMovies", listMovies);

        // DELETE LATER
        //List<Movie> listMovies = movieRepo.findAll();
        //model.addAttribute("listMovies", listMovies);

        // return movies page
        return "movies";
    }

    // single page view
    @GetMapping("/movie/{id}")
    public String showMovie(@PathVariable(name = "id") Long id, Model model){
        // find the movie by id from database
        Movie movie = movieRepo.findById(id).get();

        // model attributes for page title, movie + movie reviews
        model.addAttribute("pageTitle", movie.getTitle());
        model.addAttribute("movie", movie);
        model.addAttribute("reviewList", movie.getReviews());

        // return single movie page
        return "movie";
    }

    // add new movie
    @GetMapping("/new_movie")
    public String showNewMovieForm(Model model){
        // initiate new movie
        Movie movie = new Movie();
        // add model for page title + movie to form for data capture
        model.addAttribute("pageTitle", "New Movie");
        model.addAttribute("movie", movie);

        // return new movie form page
        return "new_movie";
    }

    // save movie
    @PostMapping ( "/save_movie")
    public String saveMovie(@ModelAttribute(name = "movie") Movie movie,
        @RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
        
        // capture filename to save in database
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // set filename
        movie.setPoster(fileName);

        // save movie data
        Movie savedMovie = movieRepo.save(movie);
        // add image file to movie-posters folder
        String uploadDir = "movie-posters/" + savedMovie.getMovieID();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        // return the new movie page
        return "redirect:/movie/" + savedMovie.getMovieID();

    }

    // edit movie
    @RequestMapping("/edit_movie/{movieID}")
    public ModelAndView showEditMovieForm(@PathVariable(name = "movieID") Long movieID) {
        ModelAndView mav = new ModelAndView("edit_movie");

        // find the movie to be edited using mapping parameter
        Movie movie = movieService.get(movieID);

        // add page title + movie to model and view object
        mav.addObject("pageTitle", "Edit " + movie.getTitle());
        mav.addObject("movie", movie);

        return mav;
    }

    // delete movie
    @RequestMapping("/delete_movie/{movieID}")
    public String deleteProduct(@PathVariable(name = "movieID") Long movieID){
        // delete movie from database
        movieService.delete(movieID);

        // return to all movies view
        return "redirect:/movies";
    }

    // search movies
    @GetMapping("/searchMovies")
    public String search(@Param("keyword") String keyword, Model model){

        // search movies table using keyword parameter
        List<Movie> listMovies = movieRepo.searchMovies(keyword);

        // add page title, movie list and keyword to model
        model.addAttribute("pageTitle", "Search Movies");
        model.addAttribute("listMovies", listMovies);
        model.addAttribute("keyword", keyword);

        // return search results
        return "search_results";
    }
}
