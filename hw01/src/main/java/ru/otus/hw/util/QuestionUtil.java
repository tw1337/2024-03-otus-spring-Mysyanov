package ru.otus.hw.util;

import org.springframework.util.CollectionUtils;
import ru.otus.hw.domain.Question;

public class QuestionUtil {
    private QuestionUtil() {

    }

    public static String formatQuestion(Question question) {
        if (question == null || isStringEmpty(question.text())) {
            return "";
        }

        var result = new StringBuilder(String.format("%s:\n", question.text()));
        var answers = question.answers();
        if (CollectionUtils.isEmpty(answers)) {
            return result.toString();
        }

        int currentAnswerNo = 1;
        for (var answer : answers) {
            if (!isStringEmpty(answer.text())) {
                result.append(String.format("%d) %s\n", currentAnswerNo++, answer.text()));
            }
        }
        return result.toString();
    }

    private static boolean isStringEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
