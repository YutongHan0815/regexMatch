package operators;

import java.io.Serializable;

public enum JoinCondition implements Condition, Serializable {
    /**
     * Verify regex: match the end position of each Span
     *
     */
    JOIN_AFTER,

    /**
     * Verify regex: match the start position of each Span
     */
    JOIN_BEFORE,

    /**
     * Verify regex: regex is exactly match the Span
     */
    JOIN_EQUAL
}
