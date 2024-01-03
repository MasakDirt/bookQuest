package org.university.bookQuest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {

    @NotBlank(message = "Write author names, it cannot be blank!")
    @JsonProperty(value = "full_name")
    private String fullName;

    @NotBlank(message = "Write authors biography please!")
    private String biography;

    @JsonProperty(value = "birth_date")
    private LocalDateTime birtDate;
}
