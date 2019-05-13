package operators;

import java.io.Serializable;

public enum VerifyCondition implements Condition, Serializable {
    /**
     * Verify regex: match the end position of each Span
     *
     */
    VERIFY_AFTER,

    /**
     * Verify regex: match the start position of each Span
     */
    VERIFY_BEFORE,

    /**
     * Verify regex: regex is exactly match the Span
     */
    VERIFY_EQUAL,

}
