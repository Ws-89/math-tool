package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.example.NumericalMethods.*;
import static org.junit.jupiter.api.Assertions.*;

class NumericalMethodsTest {

   @Test
    void testBrentMethod_Success() {
        // Funkcja x^3 - x - 2 z pierwiastkiem w około 1.521
        Function function = (x) -> x.pow(3).subtract(x).subtract(BigDecimal.valueOf(2));

        BigDecimal root = brentMethod(function, BigDecimal.valueOf(1), BigDecimal.valueOf(2), new BigDecimal("1e-6"), 100);
        BigDecimal expected = BigDecimal.valueOf(1.521);

        assertTrue(root.subtract(expected).abs().compareTo(new BigDecimal("1e-3")) < 0,
                "Znaleziony pierwiastek jest bliski oczekiwanemu " + root);
    }

    @Test
    void testBrentMethod_NoSignChange() {
        // Funkcja bez zmiany znaku w przedziale
        Function function = (x) -> x.pow(2).add(BigDecimal.valueOf(1)); // f(x) = x^2 + 1

        assertThrows(IllegalArgumentException.class, () ->
                        brentMethod(function, BigDecimal.valueOf(-2), BigDecimal.valueOf(2), new BigDecimal("1e-6"), 100),
                "Brak zmiany znaku powinien zgłosić wyjątek");
    }

    @Test
    void testBrentMethod_ConvergenceNearZero() {
        // Funkcja x^3 - x z pierwiastkami w -1, 0, 1
        Function function = (x) -> x.pow(3).subtract(x);

        BigDecimal root = brentMethod(function, BigDecimal.valueOf(-0.5), BigDecimal.valueOf(0.5), new BigDecimal("1e-6"), 100);
        assertTrue(root.abs().compareTo(new BigDecimal("1e-6")) < 0,
                "Pierwiastek w zero powinien być dokładnie znaleziony");
    }

    @Test
    void testNewtonMethod_Success() {
        // Funkcja x^3 - 2x^2 + 4x - 8 z pierwiastkiem 2
        NumericalMethods.Function function = new Function() {
            @Override
            public BigDecimal evaluate(BigDecimal x) {
                return x.pow(3).subtract(x.pow(2).multiply(BigDecimal.valueOf(2)))
                        .add(x.multiply(BigDecimal.valueOf(4)))
                        .subtract(BigDecimal.valueOf(8));
            }

            @Override
            public BigDecimal derivative(BigDecimal x) {
                return x.pow(2).multiply(BigDecimal.valueOf(3))
                        .subtract(x.multiply(BigDecimal.valueOf(4)))
                        .add(BigDecimal.valueOf(4));
            }
        };

        BigDecimal root = newtonMethod(function, BigDecimal.valueOf(1), new BigDecimal("1e-6"), 100);
        assertTrue(root.subtract(BigDecimal.valueOf(2)).abs().compareTo(new BigDecimal("1e-6")) < 0);
    }

    @Test
    void testNewtonMethod_NoConvergence() {
        // Funkcja z początkiem w problematycznym punkcie
        Function function = new Function() {
            @Override
            public BigDecimal evaluate(BigDecimal x) {
                return x.pow(2).subtract(BigDecimal.valueOf(2)); // f(x) = x^2 - 2
            }

            @Override
            public BigDecimal derivative(BigDecimal x) {
                return x.multiply(BigDecimal.valueOf(2)); // f'(x) = 2x
            }
        };

        assertThrows(ArithmeticException.class, () ->
                newtonMethod(function, BigDecimal.ZERO, new BigDecimal("1e-6"), 100));
    }

    @Test
    void testSecantMethod_Success() {
        // Funkcja x^2 - 2 z pierwiastkiem sqrt(2)
        Function function = (x) -> x.pow(2).subtract(BigDecimal.valueOf(2));

        BigDecimal root = secantMethod(function, BigDecimal.valueOf(1), BigDecimal.valueOf(2), new BigDecimal("1e-6"), 100);
        BigDecimal expected = BigDecimal.valueOf(Math.sqrt(2));

        assertTrue(root.subtract(expected).abs().compareTo(new BigDecimal("1e-6")) < 0);
    }

    @Test
    void testSecantMethod_NoConvergence() {
        // Funkcja z niepowodzeniem zbieżności
        Function function = (x) -> x.pow(2).add(BigDecimal.valueOf(1)); // f(x) = x^2 + 1

        assertThrows(ArithmeticException.class, () ->
                secantMethod(function, BigDecimal.valueOf(-1), BigDecimal.valueOf(1), new BigDecimal("1e-6"), 100));
    }

}