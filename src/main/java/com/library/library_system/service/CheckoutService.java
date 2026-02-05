package com.library.library_system.service;

import com.library.library_system.entity.*;
import com.library.library_system.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CheckoutService {

    private final BookRepository bookRepo;
    private final LoanRepository loanRepo;
    private final WaitlistRepository waitlistRepo;
    private final NotificationService notificationService;

    public CheckoutService(BookRepository bookRepo,
                           LoanRepository loanRepo,
                           WaitlistRepository waitlistRepo,
                           NotificationService notificationService) {
        this.bookRepo = bookRepo;
        this.loanRepo = loanRepo;
        this.waitlistRepo = waitlistRepo;
        this.notificationService = notificationService;
    }

    @Transactional
    public String checkout(Book book, User user) {

        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepo.save(book);

            Loan loan = new Loan();
            loan.setBook(book);
            loan.setUser(user);
            loan.setCheckoutDate(LocalDate.now());

            int days = user.getRole() == User.Role.FACULTY ? 30 : 14;
            loan.setDueDate(LocalDate.now().plusDays(days));

            loanRepo.save(loan);
            return "Checked out. Due date: " + loan.getDueDate();
        }

        int priority = user.getRole() == User.Role.FACULTY ? 1 : 2;
        waitlistRepo.save(new Waitlist(book, user, priority));

        return "No copies available. Added to waitlist.";
    }

    @Transactional
    public String returnBook(Book book, User user) {

        var loanOpt = loanRepo.findByBookIdAndUserIdAndReturnDateIsNull(book.getId(), user.getId());
        if (loanOpt.isEmpty()) return "Book not loaned.";

        Loan loan = loanOpt.get();
        loan.setReturnDate(LocalDate.now());

        long lateDays = ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate());
        if (lateDays > 0) {
            loan.setLateFee(lateDays * 2.0);
        }

        loanRepo.save(loan);

        var queue = waitlistRepo.findByBookIdOrderByPriorityAscJoinedAtAsc(book.getId());
        if (!queue.isEmpty()) {
            Waitlist next = queue.get(0);
            waitlistRepo.delete(next);
            notificationService.notifyUser(next.getUser(), "Book '" + book.getTitle() + "' is now available!");
            return "Returned. Reserved for " + next.getUser().getName();
        }

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepo.save(book);

        return "Book returned successfully.";
    }
}
