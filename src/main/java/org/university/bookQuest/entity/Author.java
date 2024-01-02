package org.university.bookQuest.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Write author names, it cannot be blank!")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "amount_of_books")
    private int booksAmount;

    @NotBlank(message = "Write authors biography please!")
    private String biography;

    @Column(name = "birth_date")
    private LocalDateTime birtDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "author")
    private List<Book> books;

    private Author(String fullName, String biography, LocalDateTime birtDate) {
        this.fullName = fullName;
        this.birtDate = birtDate;
        this.biography = biography;
    }

    public static Author of(String fullName, String biography, LocalDateTime birtDate) {
        return new Author(fullName, biography, birtDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id && booksAmount == author.booksAmount && Objects.equals(fullName, author.fullName)
                && Objects.equals(birtDate, author.birtDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, booksAmount, birtDate);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", booksAmount=" + booksAmount +
                ", birtDate=" + birtDate +
                '}';
    }
}
