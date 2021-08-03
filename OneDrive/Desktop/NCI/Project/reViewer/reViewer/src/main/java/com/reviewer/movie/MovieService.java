package com.reviewer.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repo;

    public List<Movie> listAll() {
        return repo.findAll();
    }

    public void save(Movie movie){
        repo.save(movie);
    }

    public Movie get(Long id){
        return repo.findById(id).get();
    }

    public void delete(Long id){
        repo.deleteById(id);
    }
}
