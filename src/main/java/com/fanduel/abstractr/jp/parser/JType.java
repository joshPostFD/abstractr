package com.fanduel.abstractr.jp.parser;

import antlr.Java9Parser;
import com.fanduel.abstractr.jp.parser.enums.AccessLevel;
import com.fanduel.abstractr.jp.parser.enums.Modifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class JType {

    private String packageName;
    private String typeName;
    private JType parentClass;
    private AccessLevel accessLevel = AccessLevel.PACKAGE;
    private LinkedList<String> imports = new LinkedList<>();
    private LinkedList<String> annotations = new LinkedList<>();
    private LinkedList<Modifier> modifiers = new LinkedList<>();
    private LinkedList<JField> fields = new LinkedList<>();
    private LinkedList<JMethod> methods = new LinkedList<>();
    private LinkedList<JType> innerClasses = new LinkedList<>();

    JType(Java9Parser.OrdinaryCompilationContext context) {
        processTypeDeclarationContext(context.typeDeclaration());
        processImportDeclarationContext(context.importDeclaration());
        processPackageDeclarationContext(context.packageDeclaration());
    }

    private void processPackageDeclarationContext(Java9Parser.PackageDeclarationContext context) {
        packageName = context.packageName().getText();
    }

    private void processImportDeclarationContext(List<Java9Parser.ImportDeclarationContext> contexts) {
        contexts.forEach(context -> {
            if (context.singleTypeImportDeclaration() != null) {
                imports.addLast(context.singleTypeImportDeclaration().typeName().getText());
            } else if (context.typeImportOnDemandDeclaration() != null) {
                imports.addLast(context.typeImportOnDemandDeclaration().packageOrTypeName().getText());
            } else if (context.staticImportOnDemandDeclaration() != null) {
                imports.addLast(context.staticImportOnDemandDeclaration().typeName().getText());
            } else if (context.singleStaticImportDeclaration() != null) {
                imports.addLast(context.singleStaticImportDeclaration().typeName().getText());
            }
        });
    }

    private void processTypeDeclarationContext(List<Java9Parser.TypeDeclarationContext> contexts) {
        contexts.forEach(context -> {
            // TODO: context.interfaceDeclaration()
            processClassDeclarationContext(context.classDeclaration().normalClassDeclaration());
        });
    }

    private void processClassDeclarationContext(Java9Parser.NormalClassDeclarationContext context) {
        context.classModifier().forEach(classModifierContext -> {
            if (classModifierContext.annotation() != null) {
                // TODO: annotation class. look for input variables
                if (classModifierContext.annotation().markerAnnotation() != null) {
                    annotations.addLast(classModifierContext.annotation().markerAnnotation().getText());
                } else if (classModifierContext.annotation().normalAnnotation() != null) {
                    annotations.addLast(classModifierContext.annotation().normalAnnotation().getText());
                } else if (classModifierContext.annotation().singleElementAnnotation() != null) {
                    annotations.addLast(classModifierContext.annotation().singleElementAnnotation().getText());
                }
            }
            for (ParseTree child : classModifierContext.children) {
                if (!(child instanceof Java9Parser.AnnotationContext)) {
                    Modifier.fromString(child.getText())
                            .ifPresent(modifiers::addLast);
                }
            }
        });
        modifiers.forEach(modifier -> AccessLevel.fromModifier(modifier).ifPresent(a -> accessLevel = a));
        typeName = context.identifier().getText();
        // TODO: handle super classes/interfaces, generics
        processClassBodyContext(context.classBody().classBodyDeclaration());
    }

    private void processClassBodyContext(List<Java9Parser.ClassBodyDeclarationContext> contexts) {
        contexts.forEach(context -> {
            if (context.classMemberDeclaration() != null) {
                Java9Parser.ClassMemberDeclarationContext classMemberDeclaration = context.classMemberDeclaration();
                if (classMemberDeclaration.classDeclaration() != null) {
                    // TODO: handle inner classes
                }
                if (classMemberDeclaration.fieldDeclaration() != null) {
                    fields.add(new JField(classMemberDeclaration.fieldDeclaration()));
                }
                if (classMemberDeclaration.interfaceDeclaration() != null) {
                    // TODO: handle inner interfaces
                }
                if (classMemberDeclaration.methodDeclaration() != null) {
                    methods.add(new JMethod(classMemberDeclaration.methodDeclaration()));
                }
            }
            if (context.constructorDeclaration() != null) {
                context.constructorDeclaration();
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(';')
                .append("\n\n");
        imports.forEach(i -> sb.append("import " + i + ";\n"));
        sb.append("\n");

        annotations.forEach(i -> sb.append(i + "\n"));
        sb.append(String.join(" ", modifiers.stream().map(Modifier::toString).toArray(String[]::new)))
                .append(" class ")
                .append(typeName)
                .append(" {\n\n");
        // TODO: parentClass, modifiers, inner classes
        fields.forEach(f -> sb.append(f.toString()).append("\n"));
        methods.forEach(m -> sb.append(m.toString()).append("\n\n"));
        sb.append("}");
        return sb.toString();
    }

}
