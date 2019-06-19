package edu.ics.uci.optimizer.rule;

public enum ChildPolicy {

    /**
     * Signifies that operand can have any number of children.
     */
    ANY,
    /**
     * Signifies that the operand's children must precisely match its
     * child operands, in order.
     */
    EXACT

}
