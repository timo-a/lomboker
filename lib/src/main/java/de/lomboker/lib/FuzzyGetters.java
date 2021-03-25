package de.lomboker.lib;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.lomboker.lib.TrivialGetters.isGetter;
import static de.lomboker.lib.TrivialGetters.isTrivialGetter;

public class FuzzyGetters extends Fuzzy {

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
                .forEach(FuzzyGetters::markMethod);

        return LexicalPreservingPrinter.print(cu);
    }

    private static void markMethod(MethodDeclaration md) {
        markWithStringLiteral(md, CHECK_COMMENT);
    }

    private static boolean isNonTrivialGetter(MethodDeclaration md, Set<String> fields) {
        if (!isGetter(md)) {
            return false;
        }

        if (isTrivialGetter(md, fields)) {
            return false;
        }

        return true;
    }

    public static int countFuzzyGetters(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);
        long count = wrapper.methods.stream()
                .filter(md -> isNonTrivialGetter(md, wrapper.fieldNames))
                .count();

        return (int) count;
    }
}
