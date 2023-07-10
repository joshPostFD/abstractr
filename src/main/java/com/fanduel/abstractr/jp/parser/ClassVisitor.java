package com.fanduel.abstractr.jp.parser;

import com.fanduel.abstractr.antlr.generated.Java9Parser;
import com.fanduel.abstractr.antlr.generated.Java9ParserBaseVisitor;
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
