package org.example.utils;

public class utils {

    public static double remainderTheorem(double[] dividend, double[] divisor){
        double divisor_root = -divisor[1];

        double result = 0;
        for (int i = 0; i < dividend.length; i++) {
            result += dividend[i] * Math.pow(divisor_root, dividend.length - 1 - i);
        }
        return result;
    }

    // Obliczanie wartości wielomianu w punkcie
    public static double evaluatePolynomial(double[] coeffs, double x) {
        double result = 0;
        for (int i = 0; i < coeffs.length; i++) {
            result = result * x + coeffs[i];
        }
        return result;
    }

    public static double[] hornerScheme(double[] coeefs, double divider){
        double[] result = new double[coeefs.length];

        result[0] = coeefs[0];
        for (int i = 1; i < coeefs.length; i++){
            result[i] = result[i-1] * divider + coeefs[i];
        }
        return result;
    }
}
