package com.library.library_system.repository;

import com.library.library_system.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBookIdAndUserIdAndReturnDateIsNull(Long bookId, Long userId);
    List<Loan> findByUserId(Long userId);
    List<Loan> findByReturnDateIsNullAndDueDateBefore(java.time.LocalDate date);
    List<Loan> findByUserIdAndReturnDateIsNull(Long userId);
    @Query("""
    SELECT l.book.title, COUNT(l)
    FROM Loan l
    GROUP BY l.book.title
    ORDER BY COUNT(l) DESC
    """)
    List<Object[]> mostPopularBooks();


}
