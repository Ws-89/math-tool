package org.example;

public class CauchyBounds {
    // Obliczanie granic za pomocą twierdzenia Cauchy'ego
    public static double[] cauchyBounds(double[] coeffs) {
        double maxAbs = 0;
        for (int i = 1; i < coeffs.length; i++) {
            maxAbs = Math.max(maxAbs, Math.abs(coeffs[i] / coeffs[0]));
        }
        double upperBound = 1 + maxAbs;
        return new double[]{-upperBound, upperBound};
    }
}
