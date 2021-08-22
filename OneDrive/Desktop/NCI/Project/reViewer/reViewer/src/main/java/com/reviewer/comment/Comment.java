package com.reviewer.comment;

import com.reviewer.AuditModel;
import com.reviewer.review.Review;
import com.reviewer.user.User;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment extends AuditModel implements Comparable<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentID;

    @Lob
    private String text;

    @ManyToOne
    @JoinColumn(name = "reviewID")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "movieID")
    private Review movie;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToMany(mappedBy = "commentLikes")
    private Set<User> commentLikes;

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Review getMovie() {
        return movie;
    }

    public void setMovie(Review movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(Set<User> commentLikes) {
        this.commentLikes = commentLikes;
    }

    @PreRemove
    private void removeCommentsFromReview() {
        for (Comment comment : getReview().getComments()) {
            comment.getReview().getComments().clear();
        }
    }

    @Override
    public int compareTo(Comment comment) {
        if (getCreatedAt() == null || comment.getCreatedAt() == null) {
            return 0;
        }
        return getCreatedAt().compareTo(comment.getCreatedAt());
    }
}
