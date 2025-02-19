package com.example;
/**
 * Used to help with logging performance data about the scheduling algorithims
 */
public class Logger{
    long startTime = 0;
    long endTime;
    double TAT = 0;
    double responseTime = 0;
    double submissionTime = 0;
    double executionTime = 0;

    public Logger(){
        startTime = System.currentTimeMillis();
    }

    public void setTAT(){
        this.TAT = endTime - startTime;
    }

    public void addExecutionTime(double time){
        this.executionTime += time;
    }

    public void printLogs(){
        System.out.println("    Submission Time: " + submissionTime + " ms");
        System.out.println("    Response Time: " + responseTime + " ms");
        System.out.println("    TAT: " + TAT + " ms");
        System.out.println("    Execution Time: " + executionTime + " ms");
    }
}