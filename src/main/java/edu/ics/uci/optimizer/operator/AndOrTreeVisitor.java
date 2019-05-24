package edu.ics.uci.optimizer.operator;

import io.vavr.control.Either;

public interface AndOrTreeVisitor {

    default void visitSetNode(SubsetNode subsetNode) {
        visitNode(Either.left(subsetNode));
    }

    default void visitOperatorNode(OperatorNode operatorNode) {
        visitNode(Either.right(operatorNode));
    }

    void visitNode(Either<SubsetNode, OperatorNode> node);

}

