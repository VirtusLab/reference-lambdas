package com.virtuslab.child_lambda_a.model;

public class Foo {
  private String str;

  // Default constructor needed for Jackson
  public Foo() {
  }

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