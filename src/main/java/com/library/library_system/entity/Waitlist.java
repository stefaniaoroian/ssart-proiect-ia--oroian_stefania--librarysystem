package com.library.library_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist")
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    private int priority;
    private LocalDateTime joinedAt;

    public Waitlist() {}

    public Waitlist(Book book, User user, int priority) {
        this.book = book;
        this.user = user;
        this.priority = priority;
        this.joinedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Book getBook() { return book; }
    public User getUser() { return user; }
    public int getPriority() { return priority; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
}
