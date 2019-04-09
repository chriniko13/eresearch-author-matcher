package com.eresearch.author.matcher.resource;

import org.springframework.web.context.request.async.DeferredResult;

import com.eresearch.author.matcher.dto.AuthorComparisonDto;
import com.eresearch.author.matcher.dto.AuthorMatcherResultsDto;

public interface AuthorMatcherResource {

    DeferredResult<AuthorMatcherResultsDto> authorMatcherOperation(AuthorComparisonDto authorComparisonDto);

}
