package com.library.library_system.controller;

import com.library.library_system.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/student")
public class StudentController {

    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final LoanRepository loanRepo;
    private final WaitlistRepository waitlistRepo;
    private final NotificationRepository notificationRepo;

    public StudentController(BookRepository bookRepo, UserRepository userRepo, LoanRepository loanRepo, WaitlistRepository waitlistRepo, NotificationRepository notificationRepo) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.loanRepo = loanRepo;
        this.waitlistRepo = waitlistRepo;
        this.notificationRepo = notificationRepo;
    }

    @GetMapping("/checkout")
    public String checkoutPage(@RequestParam Long bookId,
                               @RequestParam(required = false) Long userId,
                               Model model) {

        var book = bookRepo.findById(bookId).orElseThrow();
        var queue = waitlistRepo.findByBookIdOrderByPriorityAscJoinedAtAsc(bookId);
        model.addAttribute("queue", queue);
        model.addAttribute("book", book);
        model.addAttribute("users", userRepo.findAll());

        if (userId != null) {
            int pos = 1;
            for (var w : queue) {
                if (w.getUser().getId().equals(userId)) {
                    model.addAttribute("myPosition", pos);
                    break;
                }
                pos++;
            }
        }

        if (userId != null) {
            var activeLoans = loanRepo.findByUserIdAndReturnDateIsNull(userId);
            var history = loanRepo.findByUserId(userId);

            boolean hasBook = activeLoans.stream()
                    .anyMatch(l -> l.getBook().getId().equals(bookId));

            model.addAttribute("hasBook", hasBook);
            model.addAttribute("history", history);
            model.addAttribute("selectedUser", userId);
        }

        return "student-checkout";
    }


    @GetMapping("/loans")
    public String myLoans(@RequestParam Long userId, Model model) {
        model.addAttribute("user", userRepo.findById(userId).orElseThrow());
        return "student-loans";
    }

    @GetMapping("/notifications")
    public String notifications(@RequestParam Long userId, Model model) {
        model.addAttribute("notifications", notificationRepo.findByUserIdOrderByCreatedAtDesc(userId));
        return "student-notifications";
    }


}
