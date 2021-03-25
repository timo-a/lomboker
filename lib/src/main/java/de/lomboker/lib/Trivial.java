package de.lomboker.lib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Trivial {

    public static boolean onlyTrivialAnnotations(MethodDeclaration md){
        List<String> names = md.getAnnotations()
                .stream()
                .map(a-> a.getName().asString())
                .collect(Collectors.toList());

        var whiteList = Set.of("Override");

        return whiteList.containsAll(names);
    }

}
