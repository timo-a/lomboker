/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.lomboker.lib;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TrivialGetters {
    
    public static String reduceGetters(String code) {

        ClassWrapper wrapper = new ClassWrapper(code);

        CompilationUnit cu = wrapper.cu;
        LexicalPreservingPrinter.setup(cu);

        Map<String, FieldDeclaration> members = wrapper.fieldsByName;

        //all methods that return a field
        Map<String, MethodDeclaration> methods = wrapper.methods
                .stream()
                .filter(md -> isTrivialGetter(md, members.keySet()))
                .collect(Collectors.toMap(TrivialGetters::getReturned, Function.identity()));

        //intersect keys
        Set<String> intersection = new HashSet<>(members.keySet()); // use the copy constructor
        intersection.retainAll(methods.keySet());

        if (!intersection.isEmpty()) {
            cu.addImport("lombok.Getter");
        }

        //add annotation and remove method
        for (String name: intersection) {
            members.get(name).addMarkerAnnotation("Getter");
            var method = methods.get(name);
            method.removeJavaDocComment();
            method.remove();
        }

        return LexicalPreservingPrinter.print(cu);
    }

    /**
     * Determines whether a method is a Getter i.e. whether it
     * <ul>
     *   <li> is public </li>
     *   <li> has a return type (not void) </li>
     *   <li> takes no parameters </li>
     *   <li> has exactly one command that returns an expression without spaces</li>
     * </ul>
     *
     * @param md MethodDeclaration to inspect
     * @return true if method is a getter, false otherwise
     */
    public static boolean isGetter(MethodDeclaration md) {

        boolean isPublic = AccessSpecifier.PUBLIC.equals(md.getAccessSpecifier());
        boolean isNotVoid = !md.getType().isVoidType();
        boolean noParameters = md.getParameters().isEmpty();

        Optional<Expression> returnStm = getReturnStatement(md);

        return isPublic
                && isNotVoid
                && noParameters
                && returnStm.isPresent();
    }

    private static Optional<Expression> getReturnStatement(MethodDeclaration md) {
        if (md.getBody().isEmpty()) {
            return Optional.empty();
        }
        NodeList<Statement> statements = md.getBody().get().getStatements();
        if (statements.size() != 1)
            return Optional.empty();

        Statement onlyStmt = statements.getFirst().get();

        if (!onlyStmt.isReturnStmt()) {
            return Optional.empty();
        }

        Optional<Expression> oExpression = onlyStmt.asReturnStmt().getExpression();

        if (oExpression.isEmpty()
            || oExpression.get().toString().contains(" "))
            return Optional.empty();

        return oExpression;
    }

    public static boolean isTrivialGetter(MethodDeclaration md, Set<String> fields) {
        if (!isGetter(md)) {
            return false;
        }

        if (!md.getAnnotations().isEmpty())
            return false;

        //Verify that the name is what lombok would create
        String methodName = md.getNameAsString();
        String type = md.getTypeAsString();
        if (fields.stream().noneMatch(f -> nameMatch(methodName, type, f)))
            return false;

        return true;
    }

    //assumptions: type is not void
    private static boolean nameMatch(String methodName, String type, String variable) {

        boolean correctPrefix = methodName.startsWith("boolean".equals(type) ? "has" : "get");
        boolean longEnough = methodName.length() > Math.max("get".length(), "has".length());

        if (!correctPrefix || !longEnough) {
            return false;
        }

        String tail = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        return  tail.equals(variable);

    }

    /**
     * returns the name of the returned expression / a stng representation of the returned statement
     * assumptions:
     *  - there is a return statement
     *
     *  */
    private static String getReturned(MethodDeclaration md) {
        return md.getBody().get().getStatements().stream()
                .filter(Statement::isReturnStmt)
                .reduce((first, second) -> second).get() //last element
                .asReturnStmt().getExpression().get().toString();
    }

    /* Count number of getters */

    public static int countTrivialGetters(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);
        Set<String> fields = wrapper.fieldNames;
        long count = wrapper.methods.stream()
                .filter(m -> isTrivialGetter(m,fields))
                .count();

        return (int) count;
    }

    public static int countFuzzyGetters(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);
        long count = wrapper.methods.stream()
                .filter(TrivialGetters::isGetter)
                .count();

        return (int) count;
    }

}
