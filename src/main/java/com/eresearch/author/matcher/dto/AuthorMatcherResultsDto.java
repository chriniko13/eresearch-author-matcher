package com.eresearch.author.matcher.dto;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorMatcherResultsDto {

    @JsonProperty("operation-result")
    private Boolean operationResult;

    @JsonProperty("process-finished-date")
    private Instant processFinishedDate;

    @JsonProperty("comparison-results")
    private Map<StringMetricAlgorithm, StringMetricResultDto> results;

    @JsonProperty("comparison-input")
    private AuthorComparisonDto authorComparisonDto;
}
