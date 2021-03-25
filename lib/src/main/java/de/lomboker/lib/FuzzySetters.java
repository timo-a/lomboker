package de.lomboker.lib;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.lomboker.lib.TrivialGetters.isTrivialGetter;
import static de.lomboker.lib.TrivialSetters.isSetter;
import static de.lomboker.lib.TrivialSetters.isTrivialSetter;

public class FuzzySetters extends Fuzzy {

    private static final String CHECK_COMMENT = "TODO Lomboker says check this potential setter";
    /**
     * Marks all Setters that are not trivial i.e. might need manual refactoring(renaming) first.
     * */
    public static String markFuzzySetters(String code){
        CompilationUnit cu = StaticJavaParser.parse(code);
        LexicalPreservingPrinter.setup(cu);

        Set<String> fieldNames = cu.findAll(FieldDeclaration.class).stream()
                .map(fd -> fd.getVariable(0).getName().asString())
                .collect(Collectors.toSet());

        cu.findAll(MethodDeclaration.class).stream()
                .filter(md -> isNonTrivialSetter(md, fieldNames))
                .forEach(FuzzySetters::markMethod);

        return LexicalPreservingPrinter.print(cu);
    }

    public static void markMethod(MethodDeclaration md) {
        markWithStringLiteral(md, CHECK_COMMENT);
    }


    private static boolean isNonTrivialSetter(MethodDeclaration md, Set<String> fields) {
        if (!isSetter(md)) {
            return false;
        }

        if (isTrivialSetter(md, fields)) {
            return false;
        }

        return true;
    }

    public static int countFuzzySetters(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);
        long count = wrapper.methods.stream()
                .filter(md -> isNonTrivialSetter(md, wrapper.fieldNames))
                .count();

        return (int) count;
    }
}
