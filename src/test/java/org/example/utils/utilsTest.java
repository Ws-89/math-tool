package org.example.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class utilsTest {

    @Test
    void polynomialRemainderTheorem_success() {
        double[] coeffs = {1, -1, 2, 3};
        double[] divider = {1, -5};

        double expected = utils.remainderTheorem(coeffs, divider);

        assertEquals(expected, 113.0);
    }

    @Test
    void polynomialRemainderTheorem_fail() {
        double[] coeffs = {1, -1, 2, 3};
        double[] divider = {1, -5};

        double expected = utils.remainderTheorem(coeffs, divider);

        assertFalse(expected == 114.0);
    }

    @Test
    void evaluatePolynomial_success() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double result = utils.evaluatePolynomial(coeffs, 20);

        assertTrue(result == 500755292564.0);
    }

    @Test
    void evaluatePolynomial_fail() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double result = utils.evaluatePolynomial(coeffs, 20);

        assertFalse(result == 500755292563.0);
    }

    @Test
    void evaluatePolynomialAnotherValue_success() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double result = utils.evaluatePolynomial(coeffs, 15);

        assertTrue(result == 36959295504.0);
    }

    @Test
    void evaluatePolynomialAnotherValue_fail() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double result = utils.evaluatePolynomial(coeffs, 15);

        assertFalse(result == 36959295503.0);
    }

    @Test
    void hornerScheme_success(){
        double[] coeffs = {3,0,-2,1,-5};
        double[] result = utils.hornerScheme(coeffs, -2);

        double[] expected = new double[]{3,-6,10,-19,33};
        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    void hornerScheme_fail(){
        double[] coeffs = {3,0,-2,1,-5};
        double[] result = utils.hornerScheme(coeffs, -2);

        double[] expected = new double[]{3,-6,10,-19,32};
        assertFalse(Arrays.equals(expected, result));
    }
}