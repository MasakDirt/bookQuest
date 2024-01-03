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
import org.university.bookQuest.dto.AuthorRequest;
import org.university.bookQuest.mapper.AuthorMapper;
import org.university.bookQuest.service.AuthorService;
import org.university.bookQuest.service.UserService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.university.bookQuest.controller.ControllerHelper.sendRedirectAndCheckForError;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/{user-id}/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final UserService userService;
    private final AuthorMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ModelAndView getAllAuthors(
            @PathVariable("user-id") long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
            Authentication authentication, ModelMap map) {

        String sortBy = "id";
        String sortedOrder = "desc";
        map.addAttribute("page", pageNumber);
        map.addAttribute("searchText", searchText);
        map.addAttribute("is_admin", userService.isAdmin(userId));
        map.addAttribute("user_id", userId);
        map.addAttribute("dateFormatter", DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        map.addAttribute("authors", authorService.getAllAuthors(
                PageRequest.of(
                        pageNumber, 4, Sort.by(Sort.Direction.fromString(sortedOrder), sortBy)
                ), searchText)
        );
        log.info("=== GET-AUTHORS === {}", authentication.getName());

        return new ModelAndView("authors-list", map);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ModelAndView getAuthor(@PathVariable("user-id") long userId, @PathVariable long id,
                                  Authentication authentication, ModelMap map) {
        map.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        map.addAttribute("is_admin", userService.isAdmin(userId));
        map.addAttribute("author", authorService.readById(id));
        log.info("=== GET-AUTHOR-ID === {} == {}", id, authentication.getName());

        return new ModelAndView("author", map);
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ModelAndView getCreateForm(@PathVariable("user-id") long userId, ModelMap map) {
        map.addAttribute("authorRequest", new AuthorRequest());
        map.addAttribute("user_id", userId);
        return new ModelAndView("author-create", map);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void createAuthor(@PathVariable("user-id") long userId, @Valid AuthorRequest authorRequest,
                             Authentication authentication, HttpServletResponse response) {
        authorService.create(mapper.getAuthorFromAuthorRequest(authorRequest));
        log.info("=== CREATE-AUTHOR === {}", authentication.getName());
        sendRedirectAndCheckForError(response, String.format("/api/%s/authors", userId));
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getUpdateForm(@PathVariable("user-id") long userId, @PathVariable long id, ModelMap map) {
        map.addAttribute("id", id);
        map.addAttribute("user_id", userId);
        map.addAttribute("authorRequest", mapper.getAuthorRequestFromAuthor(authorService.readById(id)));
        return new ModelAndView("author-update", map);
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateAuthor(@PathVariable("user-id") long userId, @PathVariable long id,
                             @Valid AuthorRequest authorRequest, Authentication authentication, HttpServletResponse response) {
        authorService.update(id, mapper.getAuthorFromAuthorRequest(authorRequest));
        log.info("=== UPDATE-AUTHOR === {}", authentication.getName());
        sendRedirectAndCheckForError(response, String.format("/api/%s/authors", userId));
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAuthor(@PathVariable("user-id") long userId, @PathVariable long id,
                             Authentication authentication, HttpServletResponse response) {
        authorService.delete(id);
        log.info("=== DELETE-AUTHOR === {}", authentication.getName());
        sendRedirectAndCheckForError(response, String.format("/api/%s/authors", userId));
    }
}
