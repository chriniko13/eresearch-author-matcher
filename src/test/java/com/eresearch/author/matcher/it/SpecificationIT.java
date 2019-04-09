package com.eresearch.author.matcher.it;

import com.eresearch.author.matcher.EresearchAuthorMatcherApplication;
import com.eresearch.author.matcher.core.FileSupport;
import com.eresearch.author.matcher.dto.AuthorComparisonDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = EresearchAuthorMatcherApplication.class,
        properties = {"application.properties"}
)

@RunWith(SpringRunner.class)
public class SpecificationIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void author_matching_works_as_expected() throws Exception {


        // given
        String authorComparisonDtoAsString = FileSupport.getResource("test/first_case_input.json");

        AuthorComparisonDto authorComparisonDto = objectMapper.readValue(authorComparisonDtoAsString, AuthorComparisonDto.class);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<AuthorComparisonDto> httpEntity = new HttpEntity<>(authorComparisonDto, httpHeaders);


        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/author-matcher/match",
                HttpMethod.POST,
                httpEntity,
                String.class);


        // then
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
        Assert.assertNotNull(responseEntity.getBody());

        String expected = FileSupport.getResource("test/first_case_output.json");

        JSONAssert.assertEquals(expected, responseEntity.getBody(),
                new CustomComparator(JSONCompareMode.STRICT, new Customization("process-finished-date", (o1, o2) -> true)));

    }

}
