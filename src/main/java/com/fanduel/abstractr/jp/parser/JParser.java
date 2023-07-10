package com.fanduel.abstractr.jp.parser;


import com.fanduel.abstractr.antlr.generated.Java9Lexer;
import com.fanduel.abstractr.antlr.generated.Java9Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class JParser {


    public static JType parseFile(File file) {
        try {
            // Create a lexer and parser for the Java language.
            Java9Lexer lexer = new Java9Lexer(CharStreams.fromFileName(file.getAbsolutePath()));
//        Java9Lexer lexer = new Java9Lexer(CharStreams.fromFileName("/Users/joshua.post/Documents/Code/abstractr/src/main/java/com/fanduel/abstractr/TestClass.java"));
            Java9Parser parser = new Java9Parser(new CommonTokenStream(lexer));

            String fileAsAString = new String(new FileInputStream(file).readAllBytes());

            // Create a visitor that will find all of the fields in the file.
            ClassVisitor visitor = new ClassVisitor();
            parser.compilationUnit().accept(visitor);

            System.out.println(visitor.getJType().toString().replaceAll("\\s+", "").equals(fileAsAString.replaceAll("\\s+", "")));
//            System.out.println(fileAsAString.replaceAll("\\s+", ""));
//            System.out.println(visitor.getJType().toString().replaceAll("\\s+", ""));
            return visitor.getJType();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new IllegalArgumentException(file.getAbsolutePath() + " could not be read.");
        }
    }


}
