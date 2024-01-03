package org.university.bookQuest.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.university.bookQuest.dto.BookRequest;
import org.university.bookQuest.mapper.BookMapper;
import org.university.bookQuest.service.BookService;
import org.university.bookQuest.service.UserService;

import java.time.format.DateTimeFormatter;

import static org.university.bookQuest.controller.ControllerHelper.sendRedirectAndCheckForError;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/{user-id}/authors/{author-id}/books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    private final BookMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ModelAndView getAllAuthorBooks(
            @PathVariable("user-id") long userId, @PathVariable("author-id") long authorId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
            Authentication authentication, ModelMap map) {

        String sortBy = "title";
        String sortedOrder = "asc";
        map.addAttribute("page", pageNumber);
        map.addAttribute("searchText", searchText);
        map.addAttribute("is_admin", userService.isAdmin(userId));
        map.addAttribute("author_id", authorId);
        map.addAttribute("user_id", userId);
        map.addAttribute("books", bookService.getAllAuthorBooks(
                PageRequest.of(
                        pageNumber, 4, Sort.by(Sort.Direction.fromString(sortedOrder), sortBy)
                ), searchText, authorId)
        );
        log.info("=== GET-BOOKS === {}", authentication.getName());

        return new ModelAndView("books-list", map);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ModelAndView getAuthorBook(@PathVariable("user-id") long userId, @PathVariable("author-id") long authorId,
                                      @PathVariable long id, Authentication authentication, ModelMap map) {
        map.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        map.addAttribute("is_admin", userService.isAdmin(userId));
        map.addAttribute("author_id", authorId);
        map.addAttribute("book", bookService.readById(id));
        log.info("=== GET-BOOK-ID === {} == {}", id, authentication.getName());

        return new ModelAndView("book", map);
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getCreateForm(@PathVariable("user-id") long userId, @PathVariable("author-id") long authorId, ModelMap map) {
        map.addAttribute("bookRequest", new BookRequest());
        map.addAttribute("user_id", userId);
        map.addAttribute("author_id", authorId);
        return new ModelAndView("book-create", map);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void createBook(@PathVariable("user-id") long userId, @PathVariable("author-id") long authorId,
                             @Valid BookRequest bookRequest, Authentication authentication, HttpServletResponse response) {
        bookService.create(authorId, bookRequest.getTitle(), bookRequest.getDescription(), bookRequest.getPages());
        log.info("=== CREATE-BOOK === {}", authentication.getName());
        sendRedirectAndCheckForError(response, String.format("/api/%s/authors/%s/books", userId, authorId));
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getUpdateForm(@PathVariable("user-id") long userId, @PathVariable("author-id") long authorId,
                                      @PathVariable long id, ModelMap map) {
        map.addAttribute("id", id);
        map.addAttribute("author_id", authorId);
        map.addAttribute("user_id", userId);
        map.addAttribute("bookRequest", new BookRequest());
        return new ModelAndView("book-update", map);
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateBook(@PathVariable("user-id") long userId, @PathVariable("author-id") long authorId,
                           @PathVariable long id, @Valid BookRequest bookRequest,
                           Authentication authentication, HttpServletResponse response) {
        bookService.update(id, mapper.getBookFromBookRequest(bookRequest));
        log.info("=== UPDATE-BOOK === {}", authentication.getName());
        sendRedirectAndCheckForError(response, String.format("/api/%s/authors/%s/books", userId, authorId));
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(@PathVariable("user-id") long userId, @PathVariable("author-id") long authorId,
                             @PathVariable long id, Authentication authentication, HttpServletResponse response) {
        bookService.delete(id);
        log.info("=== DELETE-BOOK === {}", authentication.getName());
        sendRedirectAndCheckForError(response, String.format("/api/%s/authors/%s/books", userId, authorId));
    }
}
