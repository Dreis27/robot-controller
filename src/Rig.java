/*
Rig.java
Test rig code for students to use in LZSCC211 (Operating Systems) to run problems (e.g. 
Problem.java), and analyse the results.
Author : Dr James Stovold
Date   : Aug 17, 2022
Version: 0.1
************************
Changes: 
17/08/2022  : James Stovold : Initial File
21/08/2022  : James Stovold : Added ability to pick problem
11/11/2023  : student(3880059) : Edited file for the needs of the coursework
*/


import java.lang.System;
import java.io.*;

public class Rig { 

  Problem problem;       // abstract class interface (to be overridden by 
                         // student implementations) 
  public static void main(String[] args) {

    while (true) {

      System.out.println("\r\n=================");
      System.out.println("LZSCC211 Menu");
      System.out.println("=================\r\n");
      System.out.println("1. Option with 1 sensor");
      System.out.println("2. Option with multiple sensors");
      System.out.println("0. Exit");

      System.out.print("Pick a problem: ");
      Integer selectedOption = 0;
      BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
      String line = "";
      try {
        line = buffer.readLine();
        System.out.println();
        selectedOption = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        System.out.println("I don't know what '" + line + "' is, please input a number." );
        continue;
      } catch (IOException e) {
        System.out.println("IOException, quitting...");
      }
      if (selectedOption == 0) { break; }

      
      System.out.println("=================");

      System.out.print("Input the desired LAMBDA value: ");
      Double lambda = 0.0;
      buffer = new BufferedReader(new InputStreamReader(System.in));
      line = "";
      try {
        line = buffer.readLine();
        System.out.println();
        lambda = Double.parseDouble(line);
      } catch (NumberFormatException e) {
        System.out.println("I don't know what '" + line + "' is, please input a number." );
        continue;
      } catch (IOException e) {
        System.out.println("IOException, quitting...");
      }
      if (lambda <= 0 || lambda > 100) {
        System.out.println("Must be a positive number not larger than 100");
        continue; }
        

      System.out.println("=================");

      System.out.print("Input the desired robot's starting position (number between 0 and 1): ");
      Double start = 0.0;
      buffer = new BufferedReader(new InputStreamReader(System.in));
      line = "";
      try {
        line = buffer.readLine();
        System.out.println();
        start = Double.parseDouble(line);
      } catch (NumberFormatException e) {
        System.out.println("I don't know what '" + line + "' is, please input a number." );
        continue;
      } catch (IOException e) {
        System.out.println("IOException, quitting...");
      }
      if (start < 0 || start > 1) { 
        System.out.println("Number must be between 0 and 1");
        continue; }
      Rig rig = new Rig();
      System.out.println("Setting up problem...");
 
      switch (selectedOption) {
        case 1: // 1 sensor
          rig.problem = new SingleSensor(lambda, start);
          break;
        case 2: // multiple sensors
          rig.problem = new MultipleSensors(lambda, start);
          break;
        default:
          return;
      }

      System.out.println("Initialising problem: " + rig.problem.name());
      rig.problem.init();
      System.out.println(rig.problem.name() + " established.");
      System.out.println("Running...\r\n");
      rig.problem.go();
      return;   
      
    }
  }  
  
}