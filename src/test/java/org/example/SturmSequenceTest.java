package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.example.CauchyBounds.cauchyBounds;
import static org.junit.jupiter.api.Assertions.*;

class SturmSequenceTest {

    @Test
    void sturmSequence() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] bounds = cauchyBounds(coeffs);
        double lower = bounds[0];
        double upper = bounds[1];

        List<double[]> result = SturmSequence.sturmSequence(coeffs);

        for(double[] list: result){
            System.out.println("nowy przedział: ");
            for(double item: list){
                System.out.println(item);
            }
        }

    }



    @Test
    void differentiate_success() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.differentiate(coeffs);

        double[] target = new double[]{9,0,-63,18,135,-72,-93,108,-53};
        assertTrue(Arrays.equals(result, target));
    }

    @Test
    void differentiate_fail() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.differentiate(coeffs);

        double[] expected = new double[]{9,0,-63,18,135,-72,-93,108,-52};
        assertFalse(Arrays.equals(result, expected));
    }



    @Test
    void polyonmialDivision_success(){
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.polynomialRemainder(coeffs, new double[]{1,-2});

        double[] expected = new double[]{14};
        assertTrue(Arrays.equals(result, expected));
    }

    @Test
    void polynomialDivision_fail(){
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.polynomialRemainder(coeffs, new double[]{1,-2});

        double[] expected = new double[]{13};
        assertFalse(Arrays.equals(result, expected));
    }


    @Test
    void shiftLeft_success() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.shiftLeft(coeffs);

        double[] expected = new double[]{0, -9, 3, 27, -18, -31, 54, -53, 24};


        assertTrue(Arrays.equals(result, expected));
    }

    @Test
    void shiftLeft_fail() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.shiftLeft(coeffs);

        double[] expected = new double[]{2, -9, 3, 27, -18, -31, 54, -53, 24};


        assertFalse(Arrays.equals(result, expected));
    }

    @Test
    void trimLeadingZeros_success() {
        double[] coeffs = {0, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] expected = {-9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.trimLeadingZeros(coeffs);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    void trimLeadingZeros_fail() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};
        double[] expected = {-9, 3, 27, -18, -31, 54, -53, 24};
        double[] result = SturmSequence.trimLeadingZeros(coeffs);

        assertFalse(Arrays.equals(expected, result));
    }

    @Test
    void signChanges() {
    }

    @Test
    void countRootsSturm() {
    }

    @Test
    void bisectionRoot() {
    }
}