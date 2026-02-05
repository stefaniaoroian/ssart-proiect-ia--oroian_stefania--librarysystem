package com.library.library_system.service;

import com.library.library_system.entity.Book;
import com.library.library_system.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QrBootstrapService implements CommandLineRunner {

    private final BookRepository bookRepo;
    private final QrService qrService;

    public QrBootstrapService(BookRepository bookRepo, QrService qrService) {
        this.bookRepo = bookRepo;
        this.qrService = qrService;
    }

    @Override
    public void run(String... args) {
        List<Book> books = bookRepo.findAll();

        for (Book book : books) {
            if (book.getQrCode() == null || book.getQrCode().isEmpty()) {
                String url = "http://192.168.100.8:8086/student/checkout?bookId=" + book.getId();
                String qr = qrService.generateBase64Qr(url);
                book.setQrCode(qr);
                bookRepo.save(book);
            }
        }

        System.out.println("QR bootstrap completed. Books processed: " + books.size());
    }
}
