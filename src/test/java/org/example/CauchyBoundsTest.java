package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CauchyBoundsTest {


    @Test
    void cauchyBounds_success() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};

        double[] bounds = CauchyBounds.cauchyBounds(coeffs);
        assertArrayEquals(bounds, new double[]{-55.0,55.0});
    }

    @Test
    void cauchyBounds_fail() {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};

        double[] bounds = CauchyBounds.cauchyBounds(coeffs);
        assertFalse(Arrays.equals(bounds, new double[]{-15.0,15.0}));
    }
}