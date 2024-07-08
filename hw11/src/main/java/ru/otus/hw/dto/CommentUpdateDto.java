package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentUpdateDto {

    @NotNull(message = "Id can not be empty")
    private String id;

    @NotBlank(message = "Comment can not be empty")
    @Size(min = 2, max = 100, message = "Comment should be between 2 and 100 characters")
    private String text;

    @NotNull(message = "Book can not be empty")
    private String bookId;
}
