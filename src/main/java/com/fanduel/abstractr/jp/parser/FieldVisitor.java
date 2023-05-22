package com.fanduel.abstractr.jp.parser;


import antlr.Java9Parser;
import antlr.Java9ParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;

public class FieldVisitor extends Java9ParserBaseVisitor<Void> {

    private List<String> fields = new ArrayList<>();

    @Override
    public Void visitFieldDeclaration(Java9Parser.FieldDeclarationContext ctx) {
        // Check if the variable is a field.
        if (ctx.getParent().getRuleContexts(Java9Parser.ClassDeclarationContext.class) != null) {
            // Add the field to the list of fields.

            fields.add(ctx.getText());
            for (int index = 0; index < ctx.getChildCount(); index++) {
                System.out.println(ctx.getChild(index).getClass());
                System.out.println(ctx.getChild(index).getText());
            }
        }

        return super.visitFieldDeclaration(ctx);
    }

    public List<String> getFields() {
        return fields;
    }
}
