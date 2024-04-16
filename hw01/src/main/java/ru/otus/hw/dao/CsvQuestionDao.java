package ru.otus.hw.dao;

import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        var fileName = fileNameProvider.getTestFileName();
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("File with questions is not found");
        }

        List<QuestionDto> questionDtos;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("Error while getting input stream from file " + fileName);
            }

            questionDtos = new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(is))
                    .withMappingStrategy(getMappingStrategy())
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withExceptionHandler(e -> {
                        throw new QuestionReadException(
                                String.format("Ошибка при чтении из строки %d", e.getLineNumber()), e.getCause()
                        );
                    })
                    .build().parse();
        }
        catch (Exception e) {
            throw new QuestionReadException("Unexpected error while reading source file", e);
        }

        return questionDtos.stream()
                .filter(Objects::nonNull)
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());
    }

    private ColumnPositionMappingStrategy<QuestionDto> getMappingStrategy() {
        ColumnPositionMappingStrategy<QuestionDto> mappingStrategy = new ColumnPositionMappingStrategyBuilder<QuestionDto>().build();
        mappingStrategy.setType(QuestionDto.class);
        return mappingStrategy;
    }
}
