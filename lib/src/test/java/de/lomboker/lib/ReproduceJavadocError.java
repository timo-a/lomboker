package de.lomboker.lib;
import javax.annotation.Nonnull;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static de.lomboker.lib.TrivialSetters.isSetter;
import static de.lomboker.lib.TrivialSetters.isTrivialSetter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReproduceJavadocError {

    public static void main(String[] args) throws IOException {
        //System.out.println(annotate());
        comments4();
    }

    @Test
    public static void comments4() throws IOException {

        //added todo not found

        String input = new StringJoiner("\n")
                .add("class A {")
                .add("")
                .add("    /**")
                .add("     * set number to number")
                .add("     * @param number the number")
                .add("     */")
                .add("    public void setTheNumber(int number) {")
                .add("        number = number;")
                .add("    }")
                .add("")
                .add("}").toString();

        CompilationUnit cu = StaticJavaParser.parse(input);
        LexicalPreservingPrinter.setup(cu);

        MethodDeclaration md = cu.findFirst(MethodDeclaration.class).get();

        //md.getComment().get().setContent("new content");
        Comment comment = new JavadocComment("new content");
        md.setComment(comment);
        "1234567".trim();

        Expression e1 = new StringLiteralExpr("123");
        Expression e2 = new MethodCallExpr(e1, "trim");

        md.getBody().get().getStatements().add(0, new ExpressionStmt(e2));

        System.out.println("cu");
        System.out.println(cu);

        System.out.println("lpp");
        System.out.println(LexicalPreservingPrinter.print(cu));

    }
    @Test
    public static void comments3() throws IOException {

        //gives unsupported exception

        String input = new StringJoiner("\n")
                .add("class A {")
                .add("")
                .add("    private int number;")
                .add("")
                .add("    /**")
                //.add("     * set number to number")
                //.add("     * @param number the number")
                .add("     */")
                .add("    public void setTheNumber(int number) {")
                .add("        this.number = number;")
                .add("    }")
                .add("")
                .add("}").toString();

        CompilationUnit cu = StaticJavaParser.parse(input);
        LexicalPreservingPrinter.setup(cu);

        MethodDeclaration dmd = cu.findFirst(MethodDeclaration.class).get();

        //dmd.setJavadocComment("");

        addTodo(dmd);

        System.out.println(LexicalPreservingPrinter.print(cu));

    }

    private static MethodDeclaration addTodo(MethodDeclaration md) {
        Optional<Comment> c = md.getComment();
        if (c.isEmpty()) {
            md.setLineComment("comment");
        } else {
            String content = c.get().getContent();
            if (!content.isEmpty())
                content += "\n ";
            c.get().setContent(content + "comment");
        }
        return md;
    }




    public static void comments2() {
        MethodDeclaration md = new MethodDeclaration();
        md.setName("name");
        md.setType("void");
        //md.setJavadocComment("initial");
        //md.setLineComment("initial");
        //md.setBlockComment("initial");

        Optional<Comment> c = md.getComment();
        if (c.isEmpty()) {
            md.setLineComment("123");
        } else {
            String content = c.get().getContent();
            c.get().setContent(content + "\n TODO abc");
        }

        System.out.println(md.toString());

    }


    public static void comments() {
        MethodDeclaration md = new MethodDeclaration();
        md.setName("name");
        md.setType("void");
        md.addOrphanComment(new JavadocComment("abc"));
        md.addOrphanComment(new LineComment("def"));
        md.addOrphanComment(new BlockComment("ghi"));
        System.out.println(md.toString());
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
    private String readFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null)
            throw new IllegalArgumentException("file not found! " + fileName);

        File f = new File(resource.getFile());

        return Files.readString(f.toPath());
    }

}
