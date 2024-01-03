package org.university.bookQuest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.university.bookQuest.entity.Author;
import org.university.bookQuest.repository.AuthorRepository;
import org.university.bookQuest.repository.BookRepository;

import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author create(Author author) {
        authorRepository.saveAndFlush(author);
        log.info("registered - {}", author);
        return author;
    }

    public Author readById(long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author is not found!"));
        log.info("read - {}", author);
        return author;
    }

    public Author update(long id, Author updated) {
        Author old = readById(id);
        updated.setId(id);
        updated.setBooks(old.getBooks());
        updated.setBooksAmount(old.getBooksAmount());
        authorRepository.saveAndFlush(updated);
        log.info("updated author - {}", updated);
        return updated;
    }

    public void delete(long id) {
        authorRepository.delete(readById(id));
        log.info("author with id {} successfully deleted", id);
    }

    void updateAmountOfBooks(BookRepository bookRepository, Author author) {
        int booksAmount = bookRepository
                .findAllByAuthorId(author.getId())
                .size();
        author.setBooksAmount(booksAmount);
        authorRepository.saveAndFlush(author);
        log.info("updated amount of books {} for author {}", booksAmount, author);
    }

    public Page<Author> getAllAuthors(Pageable pageable, String searchText) {
        Page<Author> authors = sortingSelection(pageable, searchText);
        log.info("get all authors");
        return authors;
    }

    private Page<Author> sortingSelection(Pageable pageable, String searchText) {
        Page<Author> authors;
        if (checkSearchText(searchText)) {
            authors = findAuthorsWithSearchText(pageable, searchText);
        } else {
            authors = findAuthors(pageable);
        }
        return authors;
    }

    private boolean checkSearchText(String searchText) {
        return searchText != null && !searchText.trim().isEmpty();
    }

    private Page<Author> findAuthorsWithSearchText(Pageable pageable, String searchText) {
        return authorRepository.findAll()
                .stream()
                .filter(author -> author.getFullName().contains(searchText))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> new PageImpl<>(list, pageable, pageable.getPageSize())
                ));
    }

    private Page<Author> findAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }
}
