package com.eresearch.author.matcher.validator;

import com.eresearch.author.matcher.dto.AuthorComparisonDto;
import com.eresearch.author.matcher.dto.AuthorNameDto;
import com.eresearch.author.matcher.exception.DataValidationException;
import com.eresearch.author.matcher.error.EresearchAuthorMatcherError;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;

/*
NOTE: only initials can be null or empty.
 */
@Component
@Log4j
public class AuthorComparisonDtoValidator implements Validator<AuthorComparisonDto> {

    @Value("${perform.elastic.validation}")
    private String performElasticValidationStr;

    private boolean performElasticValidation;

    @PostConstruct
    public void init() {
        performElasticValidation = Boolean.valueOf(performElasticValidationStr);
    }

    @Override
    public void validate(AuthorComparisonDto authorComparisonDto) throws DataValidationException {

        // first validation.
        if (Objects.isNull(authorComparisonDto)) {
            log.error("AuthorComparisonDtoValidator#validate --- error occurred (first validation) --- authorComparisonDto = " + authorComparisonDto);
            throw new DataValidationException(EresearchAuthorMatcherError.DATA_VALIDATION_ERROR, EresearchAuthorMatcherError.DATA_VALIDATION_ERROR.getMessage());
        }

        // second validation...
        if (Objects.isNull(authorComparisonDto.getFirstAuthorName())) {
            log.error("AuthorComparisonDtoValidator#validate --- error occurred (second validation) --- authorComparisonDto = " + authorComparisonDto);
            throw new DataValidationException(EresearchAuthorMatcherError.DATA_VALIDATION_ERROR, EresearchAuthorMatcherError.DATA_VALIDATION_ERROR.getMessage());
        }

        // third validation...
        if (Objects.isNull(authorComparisonDto.getSecondAuthorName())) {
            log.error("AuthorComparisonDtoValidator#validate --- error occurred (third validation) --- authorComparisonDto = " + authorComparisonDto);
            throw new DataValidationException(EresearchAuthorMatcherError.DATA_VALIDATION_ERROR, EresearchAuthorMatcherError.DATA_VALIDATION_ERROR.getMessage());
        }

        // fourth validation...
        if (!performElasticValidation) {
            for (AuthorNameDto authorNameDto : Arrays.asList(authorComparisonDto.getFirstAuthorName(), authorComparisonDto.getSecondAuthorName())) {
                validateAuthorNameDto(authorNameDto);
            }
        }
    }

    private void validateAuthorNameDto(AuthorNameDto authorNameDto) throws DataValidationException {

        if (orReducer(
                isNull(authorNameDto.getFirstName()),
                isNull(authorNameDto.getSurname()),
                isEmpty(authorNameDto.getFirstName()),
                isEmpty(authorNameDto.getSurname()))) {
            log.error("AuthorComparisonDtoValidator#validate --- error occurred (fourth validation) --- authorComparisonDto = " + authorNameDto);
            throw new DataValidationException(EresearchAuthorMatcherError.DATA_VALIDATION_ERROR, EresearchAuthorMatcherError.DATA_VALIDATION_ERROR.getMessage());
        }

    }

    private Boolean orReducer(Boolean... booleans) {
        return Arrays.stream(booleans).reduce(false, (acc, elem) -> acc || elem);
    }

    private Boolean isEmpty(String datum) {
        return "".equals(datum);
    }

    private <T> Boolean isNull(T datum) {
        return Objects.isNull(datum);
    }

}
