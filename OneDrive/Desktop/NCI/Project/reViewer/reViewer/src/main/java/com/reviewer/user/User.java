package com.reviewer.user;

import com.reviewer.comment.Comment;
import com.reviewer.movie.Movie;
import com.reviewer.review.Review;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(nullable = false, unique = true, length = 20)
    private String userName;

    @Column(nullable = false, unique = true, length = 45)
    private String emailAddress;

    @Column(nullable = false, length = 64)
    private String password;

    private String avatar;
    private boolean enabled;

    // user's roles
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // user's reviews
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userID")
    private Set<Review> reviews = new HashSet<>();

    // user's favourites
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "favourites",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "movieID")
    )
    private Set<Movie> favourites = new HashSet<>();

    // user's seen list
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "seen",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "movieID")
    )
    private Set<Movie> seen = new HashSet<>();

    // user's want-to-watch list
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "want",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "movieID")
    )
    private Set<Movie> want = new HashSet<>();

    // user's liked reviews list
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "review_like",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "reviewID")
    )
    private Set<Review> reviewLikes = new HashSet<>();

    // user's liked comments list
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "comment_like",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "commentID")
    )
    private Set<Comment> commentLikes = new HashSet<>();

    // user's comments
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userID")
    private Set<Comment> comments = new HashSet<>();

    // reset password token
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Movie> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<Movie> favourites) {
        this.favourites = favourites;
    }

    public Set<Movie> getSeen() {
        return seen;
    }

    public void setSeen(Set<Movie> seen) {
        this.seen = seen;
    }

    public Set<Movie> getWant() {
        return want;
    }

    public void setWant(Set<Movie> want) {
        this.want = want;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Review> getReviewLikes() {
        return reviewLikes;
    }

    public void setReviewLikes(Set<Review> reviewLikes) {
        this.reviewLikes = reviewLikes;
    }

    public Set<Comment> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(Set<Comment> commentLikes) {
        this.commentLikes = commentLikes;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
}
