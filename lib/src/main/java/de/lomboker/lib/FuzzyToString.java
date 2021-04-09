package de.lomboker.lib;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.Optional;

import static de.lomboker.lib.Utils.nameMatch;

public class FuzzyToString {

    public static String reduceFuzzyToString(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);

        CompilationUnit cu = wrapper.cu;
        LexicalPreservingPrinter.setup(cu);

        //the equals method, if it exists
        Optional<MethodDeclaration> toString = wrapper.methods
                .stream()
                .filter(FuzzyToString::isToString)
                .findAny();

        if (toString.isPresent()) {
            cu.addImport("lombok.ToString");

            cu.findAll(ClassOrInterfaceDeclaration.class);

            //add annotation
            Optional<ClassOrInterfaceDeclaration> oFirstClass = cu.findFirst(ClassOrInterfaceDeclaration.class);
            if(oFirstClass.isEmpty()) {
                System.out.println("Error! no class found");
                return LexicalPreservingPrinter.print(cu);
            }

            ClassOrInterfaceDeclaration firstClass = oFirstClass.get();

            NodeList<AnnotationExpr> as = firstClass.getAnnotations();
            firstClass.addMarkerAnnotation("ToString(includeFieldNames = true)");

            //remove equals, hashCode
            toString.get().removeJavaDocComment();
            toString.get().remove();
        }

        return LexicalPreservingPrinter.print(cu);

    }

    static boolean isToString(MethodDeclaration md) {
        boolean nameMatch = nameMatch(md, "toString");
        boolean isPublic = AccessSpecifier.PUBLIC.equals(md.getAccessSpecifier());
        boolean isString = "String".equals(md.getTypeAsString());
        boolean noParameter = md.getParameters().size() == 0;

        return nameMatch
                && isPublic
                && isString
                && noParameter;
    }

}
