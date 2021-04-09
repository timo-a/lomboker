package de.lomboker.lib;

import com.github.javaparser.ast.body.MethodDeclaration;

import javax.annotation.Nonnull;

public class Utils {

    public static boolean nameMatch(MethodDeclaration md, @Nonnull String name){
        return name.equals(md.getNameAsString());
    }
}
