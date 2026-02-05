package com.library.library_system.controller;

import com.library.library_system.repository.BookRepository;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.service.CheckoutService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public CheckoutController(CheckoutService checkoutService,
                              BookRepository bookRepo,
                              UserRepository userRepo) {
        this.checkoutService = checkoutService;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam Long bookId,
                           @RequestParam Long userId) {

        System.out.println("CHECKOUT HIT: book=" + bookId + " user=" + userId);

        var book = bookRepo.findById(bookId).orElseThrow();
        var user = userRepo.findById(userId).orElseThrow();

        checkoutService.checkout(book, user);

        return "redirect:/";
    }

    @PostMapping("/checkout/return")
    public String returnBook(@RequestParam Long bookId,
                             @RequestParam Long userId) {

        System.out.println("RETURN HIT: book=" + bookId + " user=" + userId);

        var book = bookRepo.findById(bookId).orElseThrow();
        var user = userRepo.findById(userId).orElseThrow();
        checkoutService.returnBook(book, user);


        return "redirect:/student/checkout?bookId=" + bookId + "&userId=" + userId;
    }
}
