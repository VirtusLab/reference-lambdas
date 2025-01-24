package com.virtuslab.parent_lambda.model;

public class Bar {
  private Foo foo;

  public Bar() {
  } // Default constructor for Jackson

  public Bar(Foo foo) {
    this.foo = foo;
  }

  public Foo getFoo() {
    return foo;
  }

  public void setFoo(Foo foo) {
    this.foo = foo;
  }
}