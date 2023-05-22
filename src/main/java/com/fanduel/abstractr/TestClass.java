package com.fanduel.abstractr;


import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

@Data
public class TestClass {

    @NonNull
    private String field1;
    private String field2;
    private String field3;

    public TestClass() {}
    public TestClass(String field1, String field2, String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public void doRandomThing(String thing1, Integer thing2) {
        System.out.println("asdfsf");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClass testClass = (TestClass) o;
        return Objects.equals(field1, testClass.field1) && Objects.equals(field2, testClass.field2) && Objects.equals(field3, testClass.field3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2, field3);
    }
}
