package com.fanduel.abstractr.jp.parser;


import antlr.Java9Parser;
import antlr.Java9ParserBaseVisitor;
import lombok.Getter;

public class ClassVisitor extends Java9ParserBaseVisitor<Void> {

    @Getter
    private JType jType = null;

    @Override
    public Void visitOrdinaryCompilation(Java9Parser.OrdinaryCompilationContext ctx) {
        jType = new JType(ctx);
        return super.visitOrdinaryCompilation(ctx);
    }
}
