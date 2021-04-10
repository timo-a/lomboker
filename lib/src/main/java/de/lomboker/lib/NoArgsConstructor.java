package de.lomboker.lib;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import org.checkerframework.checker.units.qual.cd;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NoArgsConstructor {

    public static String reduceNoArgsConstructor(String code) {

        ClassWrapper wrapper = new ClassWrapper(code);

        CompilationUnit cu = wrapper.cu;
        LexicalPreservingPrinter.setup(cu);

        Map<String, FieldDeclaration> members = wrapper.fieldsByName;

        List<ConstructorDeclaration> constructors = cu.findAll(ConstructorDeclaration.class);

        for (var cd : constructors) {
            if (cd.getBody().isEmpty() && cd.getParameters().isEmpty()){
                Optional<ClassOrInterfaceDeclaration> oFirstClass =  cu.findFirst(ClassOrInterfaceDeclaration.class);

                if(oFirstClass.isEmpty()) {
                    System.out.println("Error! no class found. Maybe an enum? we don't support those currently");
                    return LexicalPreservingPrinter.print(cu);
                }

                ClassOrInterfaceDeclaration firstClass = oFirstClass.get();

                AccessSpecifier accessSpecifier = cd.getAccessSpecifier();
                switch (accessSpecifier){
                    case PUBLIC:
                        firstClass.addMarkerAnnotation("NoArgsConstructor");
                        break;
                    case PROTECTED:
                    case PRIVATE:
                    case PACKAGE_PRIVATE:
                        cu.addImport("lombok.AccessLevel");

                        NameExpr n = new NameExpr("AccessLevel");

                        Expression accessLevel = new FieldAccessExpr(n, accessLevelMapper(accessSpecifier));

                        MemberValuePair mvp = new MemberValuePair("access", accessLevel);
                        NodeList<MemberValuePair> nodeList = NodeList.nodeList(mvp);
                        firstClass.addAnnotation(new NormalAnnotationExpr(new Name("NoArgsConstructor"), nodeList));
                        break;
                    default:
                        ;
                }
                cu.addImport("lombok.NoArgsConstructor");


                //remove no args constructor
                cd.removeJavaDocComment();
                cd.remove();

            }
        }
        return LexicalPreservingPrinter.print(cu);
    }

    private static String accessLevelMapper(AccessSpecifier as){
        switch (as) {
            case PACKAGE_PRIVATE: return "PACKAGE";
            case PRIVATE: return "PRIVATE";
            case PROTECTED: return "PROTECTED";
            case PUBLIC: return "PUBLIC";
        }
        return "error";
    }
}
