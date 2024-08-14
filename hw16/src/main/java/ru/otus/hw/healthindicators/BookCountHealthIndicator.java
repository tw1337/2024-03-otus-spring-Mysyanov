package ru.otus.hw.healthindicators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class BookCountHealthIndicator extends AbstractHealthIndicator {

    private final BookRepository bookRepository;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        var bookCount = bookRepository.count();
        if (bookCount == 0L) {
            builder.down().withDetail("message", "No books found in library");
        }
        else {
            builder.up().withDetail("message", "There are %s books in library".formatted(bookCount));
        }
    }
}
