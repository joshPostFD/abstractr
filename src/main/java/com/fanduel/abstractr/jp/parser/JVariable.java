package com.fanduel.abstractr.jp.parser;


import com.fanduel.abstractr.antlr.generated.Java9Parser;
import com.fanduel.abstractr.jp.parser.enums.Modifier;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@AllArgsConstructor
public class JVariable {

    protected String name;
    protected String typeName;
    protected LinkedList<String> annotations = new LinkedList<>();
    protected LinkedList<Modifier> modifiers = new LinkedList<>();

    JVariable() {

    }

    JVariable(Java9Parser.FormalParameterContext formalParameterContext) {
        name = formalParameterContext.variableDeclaratorId().identifier().Identifier().getText();
        typeName = formalParameterContext.unannType().getText();
        formalParameterContext.variableModifier().forEach(modifier -> {
            Modifier.fromString(modifier.getText())
                    .ifPresent(modifiers::addLast);
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        modifiers.forEach(m -> sb.append(m.toString()).append(" "));
        sb.append(typeName).append(" ").append(name);
        return sb.toString();
    }
}
