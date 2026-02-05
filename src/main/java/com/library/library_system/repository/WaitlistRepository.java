package com.library.library_system.repository;

import com.library.library_system.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
    List<Waitlist> findByBookIdOrderByPriorityAscJoinedAtAsc(Long bookId);
}
