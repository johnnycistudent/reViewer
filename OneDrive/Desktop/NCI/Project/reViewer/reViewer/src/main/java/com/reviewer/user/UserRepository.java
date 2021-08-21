package com.reviewer.user;

import com.reviewer.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.emailAddress = ?1")
    User findByEmailAddress(String emailAddress);

    @Query("SELECT u FROM User u WHERE u.userName LIKE %?1%")
    public List<User> searchUsers(String keyword);

    public User findByResetPasswordToken(String token);

    public User findByUserName(String userName);

}
