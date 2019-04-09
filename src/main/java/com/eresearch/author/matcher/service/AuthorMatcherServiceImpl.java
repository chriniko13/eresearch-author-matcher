package com.eresearch.author.matcher.service;

import com.codahale.metrics.Timer;
import com.eresearch.author.matcher.dto.*;
import com.eresearch.author.matcher.metrics.entries.ServiceLayerMetricEntry;
import com.eresearch.author.matcher.repository.AuthorMatcherRepository;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorMatcherServiceImpl implements AuthorMatcherService {

    private static final Integer DECIMAL_PLACES = 2;
    private static final Boolean MAKE_ALL_LOWER_CASE = Boolean.TRUE;

    @Autowired
    private Clock clock;

    @Autowired
    private AuthorMatcherRepository authorMatcherRepository;

    @Value("${enable.persistence.results}")
    private String enablePersistenceForResults;

    private Map<StringMetricAlgorithm, StringMetric> stringMetricsToApply;

    @Autowired
    private ServiceLayerMetricEntry serviceLayerMetricEntry;

    @PostConstruct
    public void init() {
        initializeStringMetrics();
    }

    private void initializeStringMetrics() {
        stringMetricsToApply = new LinkedHashMap<>();

        stringMetricsToApply.put(StringMetricAlgorithm.SIMON_WHITE, StringMetrics.simonWhite());
        stringMetricsToApply.put(StringMetricAlgorithm.LEVENSHTEIN, StringMetrics.levenshtein());
        stringMetricsToApply.put(StringMetricAlgorithm.JARO, StringMetrics.jaro());
        stringMetricsToApply.put(StringMetricAlgorithm.JARO_WINKLER, StringMetrics.jaroWinkler());
        stringMetricsToApply.put(StringMetricAlgorithm.COSINE_SIMILARITY, StringMetrics.cosineSimilarity());
        stringMetricsToApply.put(StringMetricAlgorithm.EUCLIDEAN_DISTANCE, StringMetrics.euclideanDistance());
        stringMetricsToApply.put(StringMetricAlgorithm.BLOCK_DISTANCE, StringMetrics.blockDistance());
        stringMetricsToApply.put(StringMetricAlgorithm.DAMERAU_LEVENSHTEIN, StringMetrics.damerauLevenshtein());
        stringMetricsToApply.put(StringMetricAlgorithm.DICE, StringMetrics.dice());
        stringMetricsToApply.put(StringMetricAlgorithm.GENERALIZED_JACCARD, StringMetrics.generalizedJaccard());
        stringMetricsToApply.put(StringMetricAlgorithm.IDENTITY, StringMetrics.identity());
        stringMetricsToApply.put(StringMetricAlgorithm.JACCARD, StringMetrics.jaccard());
        stringMetricsToApply.put(StringMetricAlgorithm.LONGEST_COMMON_SUBSEQUENCE, StringMetrics.longestCommonSubsequence());
        stringMetricsToApply.put(StringMetricAlgorithm.LONGEST_COMMON_SUBSTRING, StringMetrics.longestCommonSubstring());
        stringMetricsToApply.put(StringMetricAlgorithm.MONGE_ELKAN, StringMetrics.mongeElkan());
        stringMetricsToApply.put(StringMetricAlgorithm.NEEDLEMAN_WUNCH, StringMetrics.needlemanWunch());
        stringMetricsToApply.put(StringMetricAlgorithm.OVERLAP_COEFFICIENT, StringMetrics.overlapCoefficient());
        stringMetricsToApply.put(StringMetricAlgorithm.Q_GRAMS_DISTANCE, StringMetrics.qGramsDistance());
        stringMetricsToApply.put(StringMetricAlgorithm.SMITH_WATERMAN, StringMetrics.smithWaterman());
        stringMetricsToApply.put(StringMetricAlgorithm.SMITH_WATERMAN_GOTOH, StringMetrics.smithWatermanGotoh());
    }

    @Override
    public AuthorMatcherResultsDto authorMatcherOperation(AuthorComparisonDto authorComparisonDto) {

        Timer.Context context = serviceLayerMetricEntry.getServiceLayerTimer().time();
        try {

            Map<StringMetricAlgorithm, StringMetricResultDto> stringMetricResults
                    = this.applyMetrics(authorComparisonDto);

            AuthorMatcherResultsDto authorMatcherResultsDto = new AuthorMatcherResultsDto();
            authorMatcherResultsDto.setOperationResult(Boolean.TRUE);
            authorMatcherResultsDto.setProcessFinishedDate(Instant.now(clock));
            authorMatcherResultsDto.setAuthorComparisonDto(authorComparisonDto);
            authorMatcherResultsDto.setResults(stringMetricResults);

            if (Boolean.valueOf(enablePersistenceForResults)) {
                authorMatcherRepository.save(authorComparisonDto, authorMatcherResultsDto);
            }

            return authorMatcherResultsDto;

        } finally {
            context.stop();
        }
    }

    private Map<StringMetricAlgorithm, StringMetricResultDto> applyMetrics(AuthorComparisonDto authorComparisonDto) {

        return stringMetricsToApply
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        m -> {
                            double comparisonResult = (double) performAuthorNamesComparison(authorComparisonDto, m.getValue());

                            StringMetricResultDto stringMetricResultDto = new StringMetricResultDto();

                            stringMetricResultDto.setComparisonResult(comparisonResult);
                            stringMetricResultDto.setComparisonResultCeil(this.round(comparisonResult,
                                    DECIMAL_PLACES,
                                    RoundingMode.CEILING));
                            stringMetricResultDto.setComparisonResultFloor(this.round(comparisonResult,
                                    DECIMAL_PLACES,
                                    RoundingMode.FLOOR));

                            return stringMetricResultDto;

                        })
                );
    }

    private float performAuthorNamesComparison(AuthorComparisonDto authorComparisonDto, StringMetric stringMetric) {

        AuthorNameDto firstAuthorName = authorComparisonDto.getFirstAuthorName();
        AuthorNameDto secondAuthorName = authorComparisonDto.getSecondAuthorName();

        String firstAuthorNameStr = firstAuthorName.getFirstName()
                + AuthorNameDto.extractInitials(firstAuthorName)
                + firstAuthorName.getSurname();

        String secondAuthorNameStr = secondAuthorName.getFirstName()
                + AuthorNameDto.extractInitials(secondAuthorName)
                + secondAuthorName.getSurname();

        return Optional
                .of(MAKE_ALL_LOWER_CASE)
                .filter(Boolean.TRUE::equals)
                .map(b -> stringMetric.compare(firstAuthorNameStr.toLowerCase(), secondAuthorNameStr.toLowerCase()))
                .orElseGet(() -> stringMetric.compare(firstAuthorNameStr, secondAuthorNameStr));

    }

    private double round(double value, int places, RoundingMode roundingMode) {
        if (places < 0) throw new IllegalArgumentException("AuthorMatcherServiceImpl#round");

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, roundingMode);

        return bd.doubleValue();
    }
}
