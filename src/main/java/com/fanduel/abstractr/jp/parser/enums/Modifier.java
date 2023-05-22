package com.fanduel.abstractr.jp.parser.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum Modifier {

    /** The modifier {@code public} */          PUBLIC("public"),
    /** The modifier {@code protected} */       PROTECTED("protected"),
    /** The modifier {@code private} */         PRIVATE("private"),
    /** The modifier {@code abstract} */        ABSTRACT("abstract"),
    /** The modifier {@code interface} */        INTERFACE("interface"),
    /**
     * The modifier {@code default}
     * @since 1.8
     */
    DEFAULT("default"),
    /** The modifier {@code static} */          STATIC("static"),

    /**
     * The modifier {@code sealed}
     * @since 17
     */
    SEALED("sealed"),

    /**
     * The modifier {@code non-sealed}
     * @since 17
     */
    NON_SEALED ("non-sealed"),
    /** The modifier {@code final} */           FINAL("final"),
    /** The modifier {@code transient} */       TRANSIENT("transient"),
    /** The modifier {@code volatile} */        VOLATILE("volatile"),
    /** The modifier {@code synchronized} */    SYNCHRONIZED("synchronized"),
    /** The modifier {@code native} */          NATIVE("native"),
    /** The modifier {@code strictfp} */        STRICTFP("strictfp");

    private String value;
    
    Modifier(String value) {
        this.value = value;
    }

    public static Optional<Modifier> fromString(String value) {
        return Arrays.stream(values())
                .filter(val -> Objects.equals(val.value, value))
                .findFirst();
    }

    /**
     * Returns this modifier's name as defined in <cite>The
     * Java Language Specification</cite>.
     * The modifier name is the {@linkplain #name() name of the enum
     * constant} in lowercase and with any underscores ("{@code _}")
     * replaced with hyphens ("{@code -}").
     * @return the modifier's name
     */
    @Override
    public String toString() {
        return value;
    }
}
