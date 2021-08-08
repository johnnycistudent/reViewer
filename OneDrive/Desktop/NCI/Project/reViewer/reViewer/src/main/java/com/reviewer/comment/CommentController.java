package com.reviewer.comment;

import com.reviewer.movie.Movie;
import com.reviewer.review.Review;
import com.reviewer.review.ReviewRepository;
import com.reviewer.user.CustomUserDetails;
import com.reviewer.user.User;
import com.reviewer.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    @Autowired
    CommentRepository commentRepo;

    @Autowired
    ReviewRepository reviewRepo;

    @Autowired
    UserRepository userRepo;


    @PostMapping(value = "/save_comment/{id}")
    public String saveComment(@PathVariable(name = "id") Long reviewID,
                             Model model, Comment newComment,
                             @AuthenticationPrincipal CustomUserDetails currentUser){

        // find signed-in user
        Long currentUserID = currentUser.getSessionUserID();
        // find current user in database
        User user = userRepo.findById(currentUserID).get();
        // find current review by id
        Review review = reviewRepo.findById(reviewID).get();

        model.addAttribute("text", newComment.getText());
        model.addAttribute("reviewID", reviewID);
        // test purposes - DELETE later
        System.out.println(reviewID);
        // test purposes - DELETE later
        System.out.println(newComment.getText());

        // save the comment
        newComment.setText(newComment.getText());
        // save comment author
        newComment.setUser(user);
        // save review comment belongs to
        newComment.setReview(review);

        // save comment
        commentRepo.save(newComment);

        return "redirect:/review/" + reviewID;
    }
}
