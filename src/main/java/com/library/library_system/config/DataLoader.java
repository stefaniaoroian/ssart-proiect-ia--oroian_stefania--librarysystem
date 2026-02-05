package com.library.library_system.config;

import com.library.library_system.entity.User;
import com.library.library_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(UserRepository userRepo) {
        return args -> {

            if (userRepo.count() == 0) {
                userRepo.save(new User("Alice Student", "alice@student.com", User.Role.STUDENT));
                userRepo.save(new User("Martin Student", "bob@student.com", User.Role.STUDENT));
                userRepo.save(new User("Elena Student", "bob@student.com", User.Role.STUDENT));
                userRepo.save(new User("Maria Student", "bob@student.com", User.Role.STUDENT));
                userRepo.save(new User("Bogdan Student", "bob@student.com", User.Role.STUDENT));
                userRepo.save(new User("Dr. Smith", "smith@faculty.com", User.Role.FACULTY));
                userRepo.save(new User("Librarian", "librarian@library.com", User.Role.LIBRARIAN));
            }
        };
    }
}
