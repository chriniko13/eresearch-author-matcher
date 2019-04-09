package com.eresearch.author.matcher.repository;


import com.eresearch.author.matcher.dto.AuthorComparisonDto;
import com.eresearch.author.matcher.dto.AuthorMatcherResultsDto;

public interface AuthorMatcherRepository {

    void save(AuthorComparisonDto authorComparisonDto, AuthorMatcherResultsDto authorMatcherResultsDto);
}
