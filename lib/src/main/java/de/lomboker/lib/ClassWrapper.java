package de.lomboker.lib;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.util.*;
import java.util.stream.Collectors;

public class ClassWrapper {

    public CompilationUnit cu;

    public List<FieldDeclaration> fields;

    public List<MethodDeclaration> methods;

    public Set<String> fieldNames;

    public Map<String, FieldDeclaration> fieldsByName = new HashMap<>();

    public ClassWrapper(String code) {
        this.cu = StaticJavaParser.parse(code);
        this.fields  = cu.findAll(FieldDeclaration.class);
        this.methods = cu.findAll(MethodDeclaration.class);
        this.fieldNames = fields.stream()
                .map(f -> f.getVariable(0).getName().asString())
                .collect(Collectors.toSet());

        for (FieldDeclaration fd: this.fields) {
            fieldsByName.put(fd.getVariable(0).getName().asString(), fd);
        }
    }

    public boolean writeAnnotationToClass(String annotation){
        Optional<ClassOrInterfaceDeclaration> oFirstClass =  cu.findFirst(ClassOrInterfaceDeclaration.class);

        if(oFirstClass.isEmpty()) {
            return false;
        }

        ClassOrInterfaceDeclaration firstClass = oFirstClass.get();

        NodeList<AnnotationExpr> as = firstClass.getAnnotations();
        firstClass.addMarkerAnnotation(annotation);
        return true;
    }
}
