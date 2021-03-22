package de.lomboker.lib;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.Set;
import java.util.stream.Collectors;

import static de.lomboker.lib.TrivialGetters.isGetter;
import static de.lomboker.lib.TrivialGetters.isTrivialGetter;

public class FuzzyGetterMarker {

    private static final String CHECK_COMMENT = "TODO Lomboker says check this potential getter";

    /**
     * Marks all Getters that are not trivial i.e. might need manual refactoring(renaming) first.
     * */
    public static String markFuzzyGetters(String code){
        CompilationUnit cu = StaticJavaParser.parse(code);
        LexicalPreservingPrinter.setup(cu);

        Set<String> fieldNames = cu.findAll(FieldDeclaration.class).stream()
                .map(fd -> fd.getVariable(0).getName().asString())
                .collect(Collectors.toSet());

        cu.findAll(MethodDeclaration.class).stream()
                .filter(md -> isNonTrivialGetter(md, fieldNames))
                .filter(md -> !isTrivialGetter(md, fieldNames))
                .forEach(md -> {md.setLineComment(CHECK_COMMENT);});

        return LexicalPreservingPrinter.print(cu);
    }

    private static boolean isNonTrivialGetter(MethodDeclaration md, Set<String> fields) {
        if (!isGetter(md)) {
            return false;
        }

        return true;
    }

}
