package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.mongo.MongoComment;

public class CommentProcessor implements ItemProcessor<MongoComment, CommentDto> {

    @Override
    public CommentDto process(MongoComment item) throws Exception {
        return new CommentDto(item.getId(), item.getText(), item.getBook().getId());
    }

}
