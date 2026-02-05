package com.library.library_system.controller;

import com.library.library_system.entity.Book;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.service.QrService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository repo;
    private final QrService qrService;

    public BookController(BookRepository repo, QrService qrService) {
        this.repo = repo;
        this.qrService = qrService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        Book saved = repo.save(book);

        String url = "http://192.168.100.8:8086/checkout?bookId=" + saved.getId();
        String qr = qrService.generateBase64Qr(url);

        saved.setQrCode(qr);
        return repo.save(saved);
    }


}
