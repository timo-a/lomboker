package de.lomboker.lib;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NoArgsConstructor {

    public static String reduceNoArgsConstructor(String code) {

        ClassWrapper wrapper = new ClassWrapper(code);

        CompilationUnit cu = wrapper.cu;
        LexicalPreservingPrinter.setup(cu);

        Map<String, FieldDeclaration> members = wrapper.fieldsByName;

        List<ConstructorDeclaration> constructors = cu.findAll(ConstructorDeclaration.class);

        for (var cd : constructors) {
            if (cd.getBody().isEmpty()){
                cu.addImport("lombok.NoArgsConstructor");

                Optional<ClassOrInterfaceDeclaration> oFirstClass =  cu.findFirst(ClassOrInterfaceDeclaration.class);

                if(oFirstClass.isEmpty()) {
                    System.out.println("Error! no class found");
                    return LexicalPreservingPrinter.print(cu);
                }

                ClassOrInterfaceDeclaration firstClass = oFirstClass.get();

                NodeList<AnnotationExpr> as = firstClass.getAnnotations();
                firstClass.addMarkerAnnotation("NoArgsConstructor");

                //remove no args constructor
                cd.removeJavaDocComment();
                cd.remove();

            }
        }
        return LexicalPreservingPrinter.print(cu);
    }
}
