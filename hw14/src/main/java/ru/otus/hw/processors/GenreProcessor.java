package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.mongo.MongoGenre;

public class GenreProcessor implements ItemProcessor<MongoGenre, GenreDto> {

    @Override
    public GenreDto process(MongoGenre item) throws Exception {
        return new GenreDto(item.getId(), item.getName());
    }

}
