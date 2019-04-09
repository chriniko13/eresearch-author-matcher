package com.eresearch.author.matcher.dao;


public interface AuthorMatcherDao {

    String getInsertQueryForSearchResultsTable();

    String getSelectQueryForSearchResultsTable();

    /*
    NOTE: this should only used for scheduler (db-cleaner).
     */
    String getDeleteQueryForSearchResultsTable();

    /*
    NOTE: this should only used for scheduler (db-cleaner).
     */
    String getResetAutoIncrementForSearchResultsTable();

    String getDropQueryForSearchResultsTable();

    String getCreationQueryForSearchResultsTable();
}
