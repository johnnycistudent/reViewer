package com.reviewer;

import static org.assertj.core.api.Assertions.assertThat;

import com.reviewer.user.User;
import com.reviewer.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        User user = new User();
        user.setEmailAddress("alex@gmail.com");
        user.setPassword("alex2020");
        user.setUserName("alex");
        user.setAvatar(null);

        User savedUser = repo.save(user);

        User existUser = entityManager.find(User.class, savedUser.getUserID());

        assertThat(existUser.getEmailAddress()).isEqualTo(user.getEmailAddress());
    }

    @Test
    public void testFindUserByEmail(){
        String emailAddress = "john@j.com";

        User user = repo.findByEmailAddress(emailAddress);

        assertThat(user).isNotNull();
    }
}
