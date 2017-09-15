package com.phuctran.popularmoviessecondstage.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuctran on 9/9/17.
 */

public class MovieResponseWrapper {
    ArrayList<MovieModel> results;

    public MovieResponseWrapper(ArrayList<MovieModel> results) {
        this.results = results;
    }

    public ArrayList<MovieModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieModel> results) {
        this.results = results;
    }
}
