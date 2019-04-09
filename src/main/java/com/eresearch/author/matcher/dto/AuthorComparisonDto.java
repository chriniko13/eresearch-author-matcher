package com.eresearch.author.matcher.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthorComparisonDto {

    @JsonProperty("first-author-name")
    private AuthorNameDto firstAuthorName;

    @JsonProperty("second-author-name")
    private AuthorNameDto secondAuthorName;

}
