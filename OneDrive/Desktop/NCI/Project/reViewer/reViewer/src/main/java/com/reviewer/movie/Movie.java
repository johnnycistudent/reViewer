package com.reviewer.movie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Movie {
    private Long movieID;
    private String title;
    private String genre;
    private String director;
    private String synopsis;

    // Constructors
    public Movie(Long movieID, String title, String genre, String director, String synopsis) {
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.synopsis = synopsis;
    }

    public Movie() {
    }

    // Getters and Setters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getMovieID() {
        return movieID;
    }

    public void setMovieID(Long id) {
        this.movieID = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
