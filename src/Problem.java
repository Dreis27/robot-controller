/*
Problem.java
Abstract interface class from which students should inherit in order to 
use the accompanying TestRig.
Author : Dr James Stovold
Date   : Aug 17, 2022
Version: 0.1
************************
Changes: 
17/08/2022  : James Stovold : Initial File
*/

public interface Problem {
  public String name();
  public void init();
  public void go();
}
