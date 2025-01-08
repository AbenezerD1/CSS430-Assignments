package com.example.fibonacci;

public class Fibonacci {

    public int calculate(int n) {
        if (n <= 1) {
            return n;
        } else {
            return calculate(n - 1) + calculate(n - 2);
        }
    }
}
