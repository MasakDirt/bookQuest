package org.university.bookQuest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    @NotBlank(message = "Write author names, it cannot be blank!")
    private String title;

    private String description;

    private int pages;
}
