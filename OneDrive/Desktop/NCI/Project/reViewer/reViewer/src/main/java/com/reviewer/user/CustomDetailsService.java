package com.reviewer.user;

import com.reviewer.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        User user = repo.findByEmailAddress(emailAddress);
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

    public void updateResetPasswordToken(String token, String emailAddress) throws UserNotFoundException {
        User user = repo.findByEmailAddress(emailAddress);

        if (user != null) {
            user.setResetPasswordToken(token);
            repo.save(user);
        } else {
            throw new UserNotFoundException("Could not find user with email " + emailAddress);
        }
    }

    public User getByResetPasswordToken(String resetPasswordToken) {
        return repo.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);

        repo.save(user);
    }
    public List<Comment> sortedByNewest(User user){
        Long id = user.getUserID();
        user = repo.findById(id).get();
        List<Comment> sortedCommentList;
        sortedCommentList = user.getCommentLikes().stream().sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return sortedCommentList;
    }
}
