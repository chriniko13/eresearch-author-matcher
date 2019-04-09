package com.eresearch.author.matcher.service;

import com.eresearch.author.matcher.dto.AuthorComparisonDto;
import com.eresearch.author.matcher.dto.AuthorMatcherResultsDto;

public interface AuthorMatcherService {

    AuthorMatcherResultsDto authorMatcherOperation(AuthorComparisonDto authorComparisonDto);
}
