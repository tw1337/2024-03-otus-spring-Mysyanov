package ru.otus.hw.services;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;

import java.util.List;

public interface BookService {
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    BookDto findById(long id);

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<BookDto> findAll();

    @PreAuthorize("hasPermission(#bookDto, 'WRITE')")
    BookDto insert(@Param("bookDto") BookCreateDto book);

    @PreAuthorize("hasPermission(#bookDto, 'WRITE')")
    BookDto update(@Param("bookDto") BookUpdateDto book);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(long id);
}
