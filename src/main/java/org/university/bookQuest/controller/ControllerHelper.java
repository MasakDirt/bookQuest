package org.university.bookQuest.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.university.bookQuest.dto.ErrorResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ControllerHelper {
    public static void sendRedirectAndCheckForError(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            log.error("Error while sending redirect - {}", e.getMessage());
            redirectionError();
        }
    }

    private static ModelAndView redirectionError() {
        ModelMap map = new ModelMap();
        map.addAttribute("errorResponse", new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Sorry, our problem with page, come back in 5 minutes😐🙏",
                "/error"
        ));
        map.addAttribute("formatter", DateTimeFormatter.ofPattern("h:mm a"));


        return new ModelAndView("error", map);
    }
}
