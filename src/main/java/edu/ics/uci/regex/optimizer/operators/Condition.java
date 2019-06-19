package edu.ics.uci.regex.optimizer.operators;

import java.io.Serializable;

public enum Condition implements Serializable {

    /**
     * Verify regex: match the end position of each Span
     *
     */
    AFTER,

    /**
     * Verify regex: match the start position of each Span
     */
    BEFORE,

    /**
     * Verify regex: regex is exactly match the Span
     */
    EQUAL,

    /**
     * Verify regex: match all the occurrences from the end position of each Span
     */
    GAP_AFTER,

    GAP_BEFORE,

}
