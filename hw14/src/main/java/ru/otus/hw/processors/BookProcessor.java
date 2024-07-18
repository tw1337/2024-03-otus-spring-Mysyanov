package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.mongo.MongoBook;

public class BookProcessor implements ItemProcessor<MongoBook, BookDto> {

    @Override
    public BookDto process(MongoBook item) throws Exception {
        return new BookDto(item.getId(), item.getTitle(), item.getAuthor().getId(), item.getGenres().getId());
    }

}
