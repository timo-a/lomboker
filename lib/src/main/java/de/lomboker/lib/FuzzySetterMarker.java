package de.lomboker.lib;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.Set;
import java.util.stream.Collectors;

import static de.lomboker.lib.TrivialSetters.isSetter;
import static de.lomboker.lib.TrivialSetters.isTrivialSetter;

public class FuzzySetterMarker {

    private static final String CHECK_COMMENT = "TODO Lomboker says check this potential setter";
    /**
     * Marks all Getters that are not trivial i.e. might need manual refactoring(renaming) first.
     * */
    public static String markFuzzySetters(String code){
        CompilationUnit cu = StaticJavaParser.parse(code);
        LexicalPreservingPrinter.setup(cu);

        Set<String> fieldNames = cu.findAll(FieldDeclaration.class).stream()
                .map(fd -> fd.getVariable(0).getName().asString())
                .collect(Collectors.toSet());

        cu.findAll(MethodDeclaration.class).stream()
                .filter(md -> isNonTrivialSetter(md, fieldNames))
                .filter(md -> !isTrivialSetter(md, fieldNames))
                .forEach(md -> {md.setLineComment(CHECK_COMMENT);});

        return LexicalPreservingPrinter.print(cu);
    }

    private static boolean isNonTrivialSetter(MethodDeclaration md, Set<String> fields) {
        if (!isSetter(md)) {
            return false;
        }

        return true;
    }

}
