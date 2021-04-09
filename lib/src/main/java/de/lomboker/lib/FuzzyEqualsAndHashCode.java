package de.lomboker.lib;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.Optional;

import de.lomboker.lib.Utils;

public class FuzzyEqualsAndHashCode {

    public static String reduceFuzzyEqualsAndHashCode(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);

        CompilationUnit cu = wrapper.cu;
        LexicalPreservingPrinter.setup(cu);

        //the equals method, if it exists
        Optional<MethodDeclaration> equals = wrapper.methods
                .stream()
                .filter(FuzzyEqualsAndHashCode::isEquals)
                .findAny();

        //the hashCode method, if it exists
        Optional<MethodDeclaration> hashCode = wrapper.methods
                .stream()
                .filter(FuzzyEqualsAndHashCode::isHashCode)
                .findAny();

        if (equals.isPresent() && hashCode.isPresent()) {
            cu.addImport("lombok.EqualsAndHashCode");

            Optional<ClassOrInterfaceDeclaration> oFirstClass =  cu.findFirst(ClassOrInterfaceDeclaration.class);

            if(oFirstClass.isEmpty()) {
                System.out.println("Error! no class found");
                return LexicalPreservingPrinter.print(cu);
            }

            ClassOrInterfaceDeclaration firstClass = oFirstClass.get();

            NodeList<AnnotationExpr> as = firstClass.getAnnotations();
            firstClass.addMarkerAnnotation("EqualsAndHashCode");

            //remove equals, hashCode
            equals.get().removeJavaDocComment();
            equals.get().remove();
            hashCode.get().removeJavaDocComment();
            hashCode.get().remove();
        }

        return LexicalPreservingPrinter.print(cu);

    }

    public static boolean isEquals(MethodDeclaration md) {
        boolean nameMatch = nameMatch(md, "equals");
        boolean isPublic = AccessSpecifier.PUBLIC.equals(md.getAccessSpecifier());
        boolean isBoolean = "boolean".equals(md.getTypeAsString());
        boolean oneParameter = md.getParameters().size() == 1;

        return nameMatch
                && isPublic
                && isBoolean
                && oneParameter
                && paramIsObject(md);
    }

    public static boolean paramIsObject(MethodDeclaration md) {
        var op = md.getParameters().getFirst();
        if(op.isEmpty()) {
            return false;
        }

        Parameter p = op.get();
        return "Object".equals(p.getTypeAsString());
    }

    static boolean isHashCode(MethodDeclaration md) {
        boolean nameMatch = nameMatch(md, "toString");
        boolean isPublic = AccessSpecifier.PUBLIC.equals(md.getAccessSpecifier());
        boolean isInt = "int".equals(md.getTypeAsString());
        boolean noParameter = md.getParameters().isEmpty();

        return nameMatch
                && isPublic
                && isInt
                && noParameter;
    }

    //todo move into utils class
    static boolean nameMatch(MethodDeclaration md, String name){
        return name.equals(md.getNameAsString());
    }

}
