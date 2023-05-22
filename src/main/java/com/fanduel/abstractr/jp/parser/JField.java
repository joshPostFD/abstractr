package com.fanduel.abstractr.jp.parser;

import antlr.Java9Parser;
import com.fanduel.abstractr.jp.parser.enums.AccessLevel;
import com.fanduel.abstractr.jp.parser.enums.Modifier;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JField extends JVariable {

    private AccessLevel accessLevel = AccessLevel.PACKAGE;

    JField(Java9Parser.FieldDeclarationContext fieldDeclaration) {
        super();
        fieldDeclaration.fieldModifier().forEach(fieldModifierContext -> {
            if (fieldModifierContext.annotation() != null) {
                this.annotations.add(fieldModifierContext.annotation().getText());
            } else {
                Modifier.fromString(fieldModifierContext.getText())
                        .ifPresent(modifiers::add);
            }
        });
        modifiers.forEach(modifier -> AccessLevel.fromModifier(modifier)
                .ifPresent(accessLevel -> this.accessLevel = accessLevel));
        fieldDeclaration.variableDeclaratorList().variableDeclarator().forEach(variableDeclaratorContext -> {
            name = variableDeclaratorContext.variableDeclaratorId().identifier().Identifier().getText();
        });
        if (fieldDeclaration.unannType().unannPrimitiveType() != null) {
            typeName = fieldDeclaration.unannType().unannPrimitiveType().getText();
        } else if (fieldDeclaration.unannType().unannReferenceType() != null) {
            typeName = fieldDeclaration.unannType().unannReferenceType().getText();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.annotations.forEach(a -> sb.append(a).append("\n"));
        if (!modifiers.isEmpty()) {
            sb.append(String.join(" ", this.modifiers.stream().map(Modifier::toString).toArray(String[]::new)))
                    .append(" ");
        }
        sb.append(typeName)
                .append(" ")
                .append(name)
                .append(";");
        return sb.toString();
    }
}
