package me.khudyakov.staticanalyzer.util;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;

import java.util.List;
import java.util.Stack;

public class TreeUtils {

    /**
     * Finds difference between two SyntaxTrees.
     * Difference - first pair of nodes that not not equal via Statement's contentEquals method
     * @param prevTree one version of SyntaxTree
     * @param curTree another version of SyntaxTree
     * @return pair of Statements from prevTree and curTree sequentially that not contentEqual
     */
    public static Pair<Statement, Statement> findDiff(SyntaxTree prevTree, SyntaxTree curTree) {
        Stack<Statement> stCur = new Stack<>();
        Stack<Statement> stPrev = new Stack<>();
        addStatementsToStack(stCur, curTree.getRoot().getChildren());
        addStatementsToStack(stPrev, prevTree.getRoot().getChildren());

        while (!stPrev.isEmpty() && !stCur.isEmpty()) {
            Statement prev = stPrev.pop();
            Statement cur = stCur.pop();
            if (!prev.contentEquals(cur)) {
                return new Pair<>(prev, cur);
            }
            addStatementsToStack(stCur, cur.getChildren());
            addStatementsToStack(stPrev, prev.getChildren());
        }

        if (stPrev.isEmpty() && stCur.isEmpty()) {
            return new Pair<>(Statement.EMPTY_STATEMENT, Statement.EMPTY_STATEMENT);
        } else if (stPrev.isEmpty()) {
            return new Pair<>(Statement.EMPTY_STATEMENT, stCur.peek());
        } else {
            return new Pair<>(stPrev.peek(), Statement.EMPTY_STATEMENT);
        }
    }

    private static void addStatementsToStack(Stack<Statement> stack, List<? extends Statement> statements) {
        for (int i = statements.size() - 1; i >= 0; i--) {
            stack.push(statements.get(i));
        }
    }
}
