package de.lomboker.lib;
import javax.annotation.Nonnull;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.StringJoiner;

public class TrivialSettersTest {

    public static void main(String[] args) {
        System.out.println(annotate());
    }

    public static String annotate() {
        String classString = new StringJoiner("\n")
                .add("class A {")
                .add("")
                .add("  @ExistingAnnotation")
                .add("  private int i;")
                .add("")
                .add("}")
                .toString();

        CompilationUnit cu = StaticJavaParser.parse(classString);
        LexicalPreservingPrinter.setup(cu);

        cu.findFirst(FieldDeclaration.class).get()
                .addAnnotation("Annotation")
                .addAnnotation(Nonnull.class)
                .addMarkerAnnotation("MarkerAnnotation");

        return LexicalPreservingPrinter.print(cu);
    }

}
