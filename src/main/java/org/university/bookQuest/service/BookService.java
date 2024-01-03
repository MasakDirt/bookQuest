package org.university.bookQuest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.university.bookQuest.entity.Book;
import org.university.bookQuest.repository.BookRepository;

import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public Book create(long authorId, String title, String description, int pages) {
        Book book = Book.of(title, description, pages);
        book.setAuthor(authorService.readById(authorId));
        bookRepository.saveAndFlush(book);
        authorService.updateAmountOfBooks(bookRepository, book.getAuthor());
        log.info("registered - {}", book);
        return book;
    }

    public Book readById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book is not found!"));
        log.info("read - {}", book);
        return book;
    }

    public Book update(long id, Book updated) {
        Book old = readById(id);
        updated.setId(id);
        updated.setAuthor(old.getAuthor());
        bookRepository.saveAndFlush(updated);
        log.info("updated book - {}", updated);
        return updated;
    }

    public void delete(long id) {
        Book book = readById(id);
        bookRepository.delete(book);
        authorService.updateAmountOfBooks(bookRepository, book.getAuthor());
        log.info("book with id {} successfully deleted", id);
    }

    public Page<Book> getAllAuthorBooks(Pageable pageable, String searchText, long authorId) {
        Page<Book> books = sortingSelection(pageable, searchText, authorId);
        log.info("get all books of author {}", authorId);
        return books;
    }

    private Page<Book> sortingSelection(Pageable pageable, String searchText, long authorId) {
        Page<Book> books;
        if (checkSearchText(searchText)) {
            books = findAuthorBooksWithSearchText(pageable, searchText, authorId);
        } else {
            books = findAuthorBooks(pageable, authorId);
        }
        return books;
    }

    private boolean checkSearchText(String searchText) {
        return searchText != null && !searchText.trim().isEmpty();
    }

    private Page<Book> findAuthorBooksWithSearchText(Pageable pageable, String searchText, long authorId) {
        return bookRepository.findAllByAuthorId(authorId, pageable)
                .stream()
                .filter(book -> book.getTitle().contains(searchText))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> new PageImpl<>(list, pageable, pageable.getPageSize())
                ));
    }

    private Page<Book> findAuthorBooks(Pageable pageable, long authorId) {
        return bookRepository.findAllByAuthorId(authorId, pageable);
    }
}
