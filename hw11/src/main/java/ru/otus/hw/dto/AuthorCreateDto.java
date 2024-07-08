package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorCreateDto {

    private String id;

    @NotBlank(message = "Full name can not be empty")
    @Size(min = 2, max = 100, message = "Full name should be between 2 and 100 characters")
    private String fullName;
}
