package de.lomboker.lib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

public class Trivial {

    public static boolean onlyTrivialAnnotations(MethodDeclaration md){
        var annotations = md.getAnnotations();
        annotations.remove(new MarkerAnnotationExpr("Override"));
        return annotations.isEmpty();
    }

}
