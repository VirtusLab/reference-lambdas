package com.virtuslab.child_lambda_b.model;

public class Baz {
  private String str;

  // Default constructor for Jackson
  public Baz() {
  }

  public Baz(String str) {
    this.str = str;
  }

  public String getStr() {
    return str;
  }

  public void setStr(String str) {
    this.str = str;
  }
}