package com.virtuslab.parent_lambda.model;

public class Foo {
  private String str;

  public Foo() {
  } // Default constructor for Jackson

  public Foo(String str) {
    this.str = str;
  }

  public String getStr() {
    return str;
  }

  public void setStr(String str) {
    this.str = str;
  }
}