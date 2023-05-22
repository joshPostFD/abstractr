module com.fanduel.abstractr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.commons.io;
    requires java.compiler;
    requires lombok;
    requires com.squareup.javapoet;
    requires org.slf4j;
    requires org.antlr.antlr4.runtime;

    opens com.fanduel.abstractr to javafx.fxml;
    exports com.fanduel.abstractr;
    exports com.fanduel.abstractr.antlr;
    opens com.fanduel.abstractr.antlr to javafx.fxml;
    exports com.fanduel.abstractr.jp;
    opens com.fanduel.abstractr.jp to javafx.fxml;
}