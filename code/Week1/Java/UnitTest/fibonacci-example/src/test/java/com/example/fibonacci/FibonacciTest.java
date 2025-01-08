package com.example.fibonacci;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FibonacciTest {

    @Test
    void testFibonacci0_3() {
        Fibonacci fibonacci = new Fibonacci();
        assertEquals(0, fibonacci.calculate(0));
        assertEquals(1, fibonacci.calculate(1));
        assertEquals(1, fibonacci.calculate(2));
        assertEquals(2, fibonacci.calculate(3));
    }

    @Test
    void testFibonacci4_6() {
        Fibonacci fibonacci = new Fibonacci();
        assertEquals(3, fibonacci.calculate(4));
        assertEquals(5, fibonacci.calculate(5));
        assertEquals(8, fibonacci.calculate(6));
    }

    @Test
    void testFibonacci7_10() {
        Fibonacci fibonacci = new Fibonacci();
        assertEquals(13, fibonacci.calculate(7));
        assertEquals(21, fibonacci.calculate(8));
        assertEquals(34, fibonacci.calculate(9));
        assertEquals(55, fibonacci.calculate(10));
    }

    @Test
    void testFibonacci0_10() {
        Fibonacci fibonacci = new Fibonacci();
        assertEquals(0, fibonacci.calculate(0));
        assertEquals(1, fibonacci.calculate(1));
        assertEquals(1, fibonacci.calculate(2));
        assertEquals(2, fibonacci.calculate(3));
        assertEquals(3, fibonacci.calculate(4));
        assertEquals(5, fibonacci.calculate(5));
        assertEquals(8, fibonacci.calculate(6));
        assertEquals(13, fibonacci.calculate(7));
        assertEquals(21, fibonacci.calculate(8));
        assertEquals(34, fibonacci.calculate(9));
        assertEquals(55, fibonacci.calculate(10));
    }

}