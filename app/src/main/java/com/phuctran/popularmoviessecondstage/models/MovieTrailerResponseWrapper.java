package com.phuctran.popularmoviessecondstage.models;

import java.util.List;

/**
 * Created by phuctran on 9/9/17.
 */

public class MovieTrailerResponseWrapper {
    List<TrailerModel> results;

    public MovieTrailerResponseWrapper(List<TrailerModel> results) {
        this.results = results;
    }

    public List<TrailerModel> getResults() {
        return results;
    }

    public void setResults(List<TrailerModel> results) {
        this.results = results;
    }
}
