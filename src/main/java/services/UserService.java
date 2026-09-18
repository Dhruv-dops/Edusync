package services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import entities.User;
import repositories.UserRepo;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String generateStudentId() {

        Random random = new Random();

        return String.valueOf(
                10000 + random.nextInt(90000)
        );
    }

    public void registerUser(User user) {

        User existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new RuntimeException("EMAIL_ALREADY_REGISTERED");
        }

        user.setRole("ROLE_STUDENT");

        user.setStudentId(generateStudentId());

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        userRepo.save(user);
    }

    public Boolean findUser(String email, String Password) {

        User user = userRepo.findByEmail(email);

        if (user != null) {

            return passwordEncoder.matches(
                    Password,
                    user.getPassword()
            );
        }

        return false;
    }
}