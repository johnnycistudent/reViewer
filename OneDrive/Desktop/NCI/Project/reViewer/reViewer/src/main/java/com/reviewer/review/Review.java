package com.reviewer.review;

import com.reviewer.AuditModel;
import com.reviewer.comment.Comment;
import com.reviewer.movie.Movie;
import com.reviewer.user.User;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "review")
public class Review extends AuditModel implements Comparable<Review> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;
    private String content;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "movieID")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToMany(mappedBy = "reviewLikes")
    private Set<User> reviewLikes;

    // review's comments
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reviewID")
    private Set<Comment> comments = new HashSet<>();

    // getters and setters

    public Long getReviewID() {
        return reviewID;
    }

    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getReviewLikes() {
        return reviewLikes;
    }

    public void setReviewLikes(Set<User> reviewLikes) {
        this.reviewLikes = reviewLikes;
    }

    @PreRemove
    private void removeReviewFromUsers() {
        for (User user : getReviewLikes()) {
            user.getFavourites().remove(this);
            //user.getComments().clear();
        }
    }


    @Override
    public int compareTo(Review review) {
        if (getCreatedAt() == null || review.getCreatedAt() == null) {
            return 0;
        }
        return getCreatedAt().compareTo(review.getCreatedAt());
    }
}
