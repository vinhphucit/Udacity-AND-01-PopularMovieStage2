package com.phuctran.popularmoviessecondstage.enums;

import java.io.Serializable;

/**
 * Created by phuctran on 9/9/17.
 */

public enum MovieSortType implements Serializable {
    MOST_POPULART("popular"),
    HIGHEST_RATED("top_rated"),
    FAVOURITE("favourite");
    private final String text;

    /**
     * @param text
     */
    MovieSortType(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
