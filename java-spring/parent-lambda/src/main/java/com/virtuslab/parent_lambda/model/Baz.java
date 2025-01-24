package com.virtuslab.parent_lambda.model;

public class Baz {
  private String str;

  public Baz() {
  } // Default constructor for Jackson

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