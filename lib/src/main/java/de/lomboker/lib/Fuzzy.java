package de.lomboker.lib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.Optional;

public class Fuzzy {

    protected static void markWithStringLiteral(MethodDeclaration md, String message) {
        Expression info = new StringLiteralExpr(message);
        Expression e2 = new MethodCallExpr(info, "trim");

        //it is callers responsibility to ensure that there is a body
        md.getBody().get().getStatements().add(new ExpressionStmt(e2));
        //md.getBody().get().getStatements().add(0, new ExpressionStmt(e2));
        //inserting in first place messes up indentation
    }

    /* Discontinued: I was unable to find a way to add comments on their own line
    * so they can be easily be removed
    * and coexist with existing comments */
    @Deprecated
    private static MethodDeclaration addTodo(MethodDeclaration md, String message) {
        Optional<Comment> c = md.getComment();
        if (c.isEmpty()) {
            md.setLineComment(message);
        } else {
            String content = c.get().getContent();
            if (!content.isEmpty())
                content += "\n ";
            c.get().setContent(content + message);
        }
        return md;
    }


}
