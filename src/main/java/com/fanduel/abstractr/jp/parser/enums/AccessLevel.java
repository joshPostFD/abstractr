package com.fanduel.abstractr.jp.parser.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum AccessLevel {

    PUBLIC("public", Modifier.PUBLIC),
    PRIVATE("private", Modifier.PRIVATE),
    PROTECTED("protected", Modifier.PROTECTED),
    PACKAGE("package", null)
    ;

    private final String value;
    private final Modifier modifier;

    AccessLevel(String value, Modifier modifier) {
        this.value = value;
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return value;
    }

    public Modifier toModifier() {
        return modifier;
    }

    public static Optional<AccessLevel> fromString(String value) {
        return Arrays.stream(values())
                .filter(val -> Objects.equals(val.value, value))
                .findFirst();
    }

    public static Optional<AccessLevel> fromModifier(Modifier modifier) {
        return Arrays.stream(values())
                .filter(val -> Objects.equals(val.modifier, modifier))
                .findFirst();
    }

}
