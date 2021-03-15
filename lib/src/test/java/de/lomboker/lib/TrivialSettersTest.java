package de.lomboker.lib;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import org.junit.jupiter.api.Test;

public class TrivialSettersTest {

    @Test
    public void testSetter() {
        String classString = "class A { private int i; public void setI(int j){ this.i=j;}}";

        CompilationUnit cu = StaticJavaParser.parse(classString);

        MethodDeclaration md = cu.findFirst(MethodDeclaration.class).get();
        TrivialSetters.isSetter(md);

    }

}
