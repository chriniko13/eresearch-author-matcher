package com.eresearch.author.matcher.dao;

import org.springframework.stereotype.Component;

@Component
public class AuthorMatcherDaoImpl implements AuthorMatcherDao {

    @Override
    public String getInsertQueryForSearchResultsTable() {
        return "INSERT INTO author_matcher.search_results(author_comparison_input, author_matcher_results, creation_timestamp) VALUES (?, ?, ?)";
    }

    @Override
    public String getSelectQueryForSearchResultsTable() {
        return "SELECT * FROM author_matcher.search_results";
    }

    @Override
    public String getDeleteQueryForSearchResultsTable() {
        return "DELETE FROM author_matcher.search_results";
    }

    @Override
    public String getResetAutoIncrementForSearchResultsTable() {
        return "ALTER TABLE author_matcher.search_results AUTO_INCREMENT = 1";
    }

    @Override
    public String getDropQueryForSearchResultsTable() {
        return "DROP TABLE IF EXISTS author_matcher.search_results";
    }

    @Override
    public String getCreationQueryForSearchResultsTable() {
        return "CREATE TABLE IF NOT EXISTS author_matcher.search_results(id BIGINT(20) NOT NULL AUTO_INCREMENT, author_comparison_input MEDIUMTEXT, author_matcher_results LONGTEXT, creation_timestamp TIMESTAMP NULL DEFAULT NULL, PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
