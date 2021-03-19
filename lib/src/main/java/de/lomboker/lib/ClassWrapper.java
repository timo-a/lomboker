package de.lomboker.lib;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
}
