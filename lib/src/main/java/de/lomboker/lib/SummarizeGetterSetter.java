package de.lomboker.lib;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static de.lomboker.lib.Utils.nameMatch;

public class SummarizeGetterSetter {


    public static String summarizeGetters(String code) {
        return summarizeFieldAnnotation(code, "Getter");
    }

    public static String summarizeSetters(String code) {
        return summarizeFieldAnnotation(code, "Setter");
    }

    public static String summarizeFieldAnnotation(String code, final String annotation) {
        ClassWrapper wrapper = new ClassWrapper(code);

        CompilationUnit cu = wrapper.cu;
        LexicalPreservingPrinter.setup(cu);

        List<FieldDeclaration> allNonStatic = wrapper.fields.stream()
                .filter(fd -> !fd.isStatic()).collect(Collectors.toList());

        MarkerAnnotationExpr mae = new MarkerAnnotationExpr(annotation);

        Predicate<FieldDeclaration> hasAnnotation = fd -> fd.getAnnotations()
                                                            .contains(mae);

        boolean positivelyAnnotated = allNonStatic.stream().anyMatch(hasAnnotation);
        boolean fullyAnnotated = allNonStatic.stream().allMatch(hasAnnotation);

        if (positivelyAnnotated && fullyAnnotated) {

            if(!wrapper.writeAnnotationToClass(annotation)) {
                System.out.println("Error! no class found");
                return LexicalPreservingPrinter.print(cu);
            }


            allNonStatic.forEach(fd ->
                    fd.getAnnotations().remove(mae));

        }

        return LexicalPreservingPrinter.print(cu);

    }

}
