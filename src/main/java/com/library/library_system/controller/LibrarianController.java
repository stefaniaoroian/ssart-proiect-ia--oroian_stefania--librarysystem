package com.library.library_system.controller;

import com.library.library_system.repository.LoanRepository;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.config.RabbitConfig;
import com.library.library_system.messaging.NotificationPayload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;



@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final RabbitTemplate rabbitTemplate;

    public LibrarianController(LoanRepository loanRepo, BookRepository bookRepo, RabbitTemplate rabbitTemplate) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("overdue", loanRepo.findByReturnDateIsNullAndDueDateBefore(java.time.LocalDate.now()));
        model.addAttribute("books", bookRepo.findAll());
        model.addAttribute("stats", loanRepo.mostPopularBooks());

        return "librarian-dashboard";
    }

    @PostMapping("/reminder")
    public String sendReminder(@RequestParam Long loanId) {

        var loan = loanRepo.findById(loanId).orElseThrow();

        rabbitTemplate.convertAndSend(
                RabbitConfig.QUEUE_NAME,
                new NotificationPayload(loan.getUser(), "Reminder: Book '" + loan.getBook().getTitle() + "' is overdue!")
        );

        return "redirect:/librarian/dashboard";
    }


}
