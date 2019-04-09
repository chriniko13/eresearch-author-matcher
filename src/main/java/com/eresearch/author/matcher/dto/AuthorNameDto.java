package com.eresearch.author.matcher.dto;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthorNameDto {

    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("initials")
    private String initials;

    @JsonProperty("surname")
    private String surname;


    public static String extractInitials(AuthorNameDto authorNameDto) {

        return Optional
                .ofNullable(authorNameDto)
                .map(AuthorNameDto::getInitials)
                .filter(Objects::nonNull)
                .filter(initials -> !"".equals(initials))
                .orElse("");
    }
}
