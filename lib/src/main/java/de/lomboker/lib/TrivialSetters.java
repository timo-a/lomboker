/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.lomboker.lib;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TrivialSetters extends Trivial {
    
    public static String reduceSetters(String code) {

        ClassWrapper wrapper = new ClassWrapper(code);

        CompilationUnit cu = wrapper.cu;
        LexicalPreservingPrinter.setup(cu);

        Map<String, FieldDeclaration> members = wrapper.fieldsByName;

        //all methods that return a field
        Map<String, MethodDeclaration> methods = wrapper.methods
                .stream()
                .filter(md -> TrivialSetters.isTrivialSetter(md, members.keySet()))
                .collect(Collectors.toMap(TrivialSetters::getSetVariable, Function.identity()));

        //intersect keys
        Set<String> intersection = new HashSet<>(members.keySet()); // use the copy constructor
        intersection.retainAll(methods.keySet());


        if (!intersection.isEmpty()) {
            cu.addImport("lombok.Setter");
        }

        //add annotation and remove method
        for (String name: intersection) {
            members.get(name).addMarkerAnnotation("Setter");
            var method = methods.get(name);
            method.removeJavaDocComment();
            method.remove();
        }

        return LexicalPreservingPrinter.print(cu);
    }

    /**
     * Determines whether a method is a Setter i.e. whether it
     * <ul>
     *   <li> is public </li>
     *   <li> is of "type" void</li>
     *   <li> takes one parameters </li>
     *   <li> has exactly one command that assigns the parameter to a variable</li>
     * </ul>
     *
     * There are no more constraints, the name of the method and the name of
     * the set variable for example are ignored
     *
     * @param md MethodDeclaration to inspect
     * @return true if method is a setter, false otherwise
     */
    public static boolean isSetter(MethodDeclaration md) {

        boolean isPublic = AccessSpecifier.PUBLIC.equals(md.getAccessSpecifier());
        boolean isVoid = md.getType().isVoidType();
        boolean oneParameter = md.getParameters().size() == 1;

        return isPublic
                && isVoid
                && oneParameter
                && getSetStatement(md).isPresent();
    }

    //TODO return bool instead, document
    /**
     * Assumptions made
     *  - the method has one parameter
     * */
    private static Optional<Expression> getSetStatement(MethodDeclaration md) {
        if (md.getBody().isEmpty())
            return Optional.empty();

        Parameter parameter = md.getParameter(0);

        String parameterName = parameter.getNameAsString();

        NodeList<Statement> statements = md.getBody().get().getStatements();
        if (statements.size() != 1)
            return Optional.empty();

        Statement onlyStmt = statements.getFirst().get();

        if (!onlyStmt.isExpressionStmt())
            return Optional.empty();

        Expression e = onlyStmt.asExpressionStmt().getExpression();

        if (!e.isAssignExpr())
            return Optional.empty();

        AssignExpr ae = e.asAssignExpr();

        // confirm that the expression is an assignment
        var op = ae.getOperator();

        if (op != AssignExpr.Operator.ASSIGN)
            return Optional.empty();
// Hey, this is a comment


        Expression target = ae.getTarget();

        Expression value = ae.getValue();
        String valueS = value.toString();

        boolean isFieldAssignment = target.isFieldAccessExpr() //target is "this.x"
                || !target.toString().equals(parameterName); //or at least not the parameter

        if (!isFieldAssignment)
            return Optional.empty();

        if (!valueS.equals(parameterName))
            return Optional.empty();

        return Optional.of(ae);
    }

    /**
     * more restrictive filter in addition to passing isSetter the method must
     * <ul>
     *   <li> match in name with a field </li>
     * </ul>
     *
     */
    public static boolean isTrivialSetter(MethodDeclaration md, Set<String> fields) {
        if (!isSetter(md)) {
            return false;
        }

        if (!onlyTrivialAnnotations(md))
            return false;

        //Verify that the name is what lombok would create
        String methodName = md.getNameAsString();
        if (fields.stream().noneMatch(f -> nameMatch(methodName, f)))
            return false;

        return true;
    }

    private static boolean nameMatch(String methodName, String variable) {
        boolean correctPrefix = methodName.startsWith("set");
        boolean longEnough = methodName.length() > "set".length();

        if (!correctPrefix || !longEnough) {
            return false;
        }

        String tail = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        return  tail.equals(variable);

    }

    /**
     * returns the name of the returned expression / a stng representation of the returned statement
     * assumptions:
     *  - there is a last statement and it sets a variable
     *
     *  */
    private static String getSetVariable(MethodDeclaration md) {
        ExpressionStmt e =  md.getBody().get().getStatements().getLast().get().asExpressionStmt();
        String variableName = e.getExpression().asAssignExpr().getTarget().toString();
        return variableName.replace("this.","");
    }

    /* Count number of getters */

    public static int countTrivialSetters(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);
        Set<String> fields = wrapper.fieldNames;
        long count = wrapper.methods.stream()
                .filter(m -> isTrivialSetter(m,fields))
                .count();

        return (int) count;
    }

    public static int countFuzzySetters(String code) {
        ClassWrapper wrapper = new ClassWrapper(code);
        long count = wrapper.methods.stream()
                .filter(TrivialSetters::isSetter)
                .count();

        return (int) count;
    }

}
