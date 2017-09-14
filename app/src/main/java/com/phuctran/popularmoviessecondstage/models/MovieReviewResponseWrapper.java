package com.phuctran.popularmoviessecondstage.models;

import java.util.List;

/**
 * Created by phuctran on 9/9/17.
 */

public class MovieReviewResponseWrapper {
    List<ReviewModel> results;

    public MovieReviewResponseWrapper(List<ReviewModel> results) {
        this.results = results;
    }

    public List<ReviewModel> getResults() {
        return results;
    }

    public void setResults(List<ReviewModel> results) {
        this.results = results;
    }
}
