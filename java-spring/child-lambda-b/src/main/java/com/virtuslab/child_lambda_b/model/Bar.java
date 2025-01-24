package com.virtuslab.child_lambda_b.model;

public class Bar {
  private Foo foo;

  // Default constructor for Jackson
  public Bar() {
  }

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