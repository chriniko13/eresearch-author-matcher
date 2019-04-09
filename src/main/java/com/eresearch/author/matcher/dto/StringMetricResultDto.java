package com.eresearch.author.matcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StringMetricResultDto {

    @JsonProperty("comparison-result")
    private Double comparisonResult;

    @JsonProperty("comparison-result-floor")
    private Double comparisonResultFloor;

    @JsonProperty("comparison-result-ceil")
    private Double comparisonResultCeil;

}
