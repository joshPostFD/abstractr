package com.fanduel.abstractr.jp.parser;


import com.fanduel.abstractr.antlr.generated.Java9Parser;
import com.fanduel.abstractr.jp.parser.enums.AccessLevel;
import com.fanduel.abstractr.jp.parser.enums.Modifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.RuleContext;

import java.util.LinkedList;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class JMethod {

    private AccessLevel accessLevel = AccessLevel.PACKAGE;
    private String methodName;
    private LinkedList<Modifier> modifiers = new LinkedList<>();
    private LinkedList<JVariable> inputParameters = new LinkedList<>();
    private String returnType;
    private String contents;

    JMethod(Java9Parser.MethodDeclarationContext methodDeclaration) {
        methodDeclaration.methodModifier()
                .stream()
                .map(RuleContext::getText)
                .forEach(modifier -> Modifier.fromString(modifier)
                        .ifPresent(modifiers::addLast));
        modifiers.forEach(modifier -> AccessLevel.fromModifier(modifier)
                .ifPresent(accessLevel -> this.accessLevel = accessLevel));
        methodName = methodDeclaration.methodHeader().methodDeclarator().identifier().Identifier().getText();
        if (methodDeclaration.methodHeader().methodDeclarator().formalParameterList() != null) {
            Java9Parser.FormalParameterListContext formalParameterListContext = methodDeclaration.methodHeader().methodDeclarator().formalParameterList();
            if (formalParameterListContext.formalParameters() != null) {
                formalParameterListContext.formalParameters().formalParameter()
                        .forEach(formalParameterContext -> inputParameters.add(new JVariable(formalParameterContext)));
            }
            if (formalParameterListContext.lastFormalParameter() != null) {
                Java9Parser.FormalParameterContext formalParameterContext = formalParameterListContext.lastFormalParameter().formalParameter();
                inputParameters.add(new JVariable(formalParameterContext));
            }
        }
        returnType = methodDeclaration.methodHeader().result().getText();
        // TODO: handle method annotations
        contents = methodDeclaration.methodBody().getText();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // TODO: modifiers
        sb.append(accessLevel)
                .append(" ")
                .append(returnType)
                .append(" ")
                .append(methodName)
                .append('(');
        for (int index = 0; index < inputParameters.size(); index++) {
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(inputParameters.get(index));
        }
        sb.append(") ")
                .append(contents);
        return sb.toString();
    }
}
