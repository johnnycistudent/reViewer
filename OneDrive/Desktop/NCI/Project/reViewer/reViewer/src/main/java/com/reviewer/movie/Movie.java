package com.reviewer.movie;

import com.reviewer.review.Review;
import com.reviewer.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieID;
    private String title;
    private int year;
    private String genre;
    private String director;
    private String synopsis;
    @Column(nullable = true)
    private String poster;

    @OneToMany
    @JoinColumn(name = "movieID")
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "favourites")
    private Set<User> favourites;

    @ManyToMany(mappedBy = "seen")
    private Set<User> seen;

    @ManyToMany(mappedBy = "want")
    private Set<User> want;

    @Column(name="avgRating", columnDefinition = "int default 0")
    private int avgRating;

    // Constructors
    public Movie(Long movieID, String title, int year, String genre, String director, String synopsis, String poster) {
        this.movieID = movieID;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.synopsis = synopsis;
        this.poster = poster;
    }

    public Movie() {
    }

    // Getters and Setters


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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Transient
    public String getPosterImagePath(){
        if (poster == null || movieID == null) return null;

        return "/movie-posters/" + movieID + "/" + poster;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<User> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<User> favourites) {
        this.favourites = favourites;
    }

    public Set<User> getSeen() {
        return seen;
    }

    public void setSeen(Set<User> seen) {
        this.seen = seen;
    }

    public Set<User> getWant() {
        return want;
    }

    public void setWant(Set<User> want) {
        this.want = want;
    }

    public int getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(int avgRating) {
        this.avgRating = avgRating;
    }
}
