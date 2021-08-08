package com.reviewer.movie;

import com.reviewer.FileUploadUtil;
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
        List<Movie> listMovies = movieService.listAll();
        model.addAttribute("listMovies", listMovies);

        return "movies";
    }

    @GetMapping("/movie/{id}")
    public String showMovie(@PathVariable(name = "id") Long id, Model model){
        Movie movie = movieRepo.findById(id).get();

        model.addAttribute("movie", movie);
        model.addAttribute("reviewList", movie.getReviews());

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
    @PostMapping ( "/save_movie")
    public String saveMovie(@ModelAttribute(name = "movie") Movie movie,
        @RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
        
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        movie.setPoster(fileName);

        Movie savedMovie = movieRepo.save(movie);

        String uploadDir = "movie-posters/" + savedMovie.getMovieID();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/movie/" + savedMovie.getMovieID();

    }

    // edit movie
    @RequestMapping("/edit_movie/{movieID}")
    public ModelAndView showEditMovieForm(@PathVariable(name = "movieID") Long movieID) {
        ModelAndView mav = new ModelAndView("edit_movie");

        Movie movie = movieService.get(movieID);

        mav.addObject("movie", movie);

        return mav;
    }

    // delete movie
    @RequestMapping("/delete_movie/{movieID}")
    public String deleteProduct(@PathVariable(name = "movieID") Long movieID){
        movieService.delete(movieID);

        return "redirect:/movies";
    }

    @GetMapping("/search")
    public String search(@Param("keyword") String keyword, Model model){

        List<Movie> searchResult = movieService.search(keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("searchResult", searchResult);

        return "search_results";
    }
}
