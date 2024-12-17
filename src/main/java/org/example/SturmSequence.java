package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.example.CauchyBounds.cauchyBounds;
import static org.example.utils.utils.evaluatePolynomial;

public class SturmSequence {

    // Stworzenie ciągu Sturma
    public static List<double[]> sturmSequence(double[] coeffs) {
        List<double[]> sequence = new ArrayList<>();
        sequence.add(coeffs);
        sequence.add(differentiate(coeffs));
        int maxIterations = 100; // Zabezpieczenie przed nieskończoną pętlą
        int iterations = 0;

        while (iterations < maxIterations) {
            double[] prev = sequence.get(sequence.size() - 2);
            double[] curr = sequence.get(sequence.size() - 1);
            double[] remainder = polynomialRemainder(prev, curr);

            if (remainder.length == 1 && Math.abs(remainder[0]) < 1e-6) {
                break;
            }

            for (int i = 0; i < remainder.length; i++) {
                remainder[i] = -remainder[i];
            }

            sequence.add(remainder);
            iterations++;

            if (iterations == maxIterations) {
                System.out.println("Uwaga: osiągnięto maksymalną liczbę iteracji w ciągu Sturma!");
                return sequence;
            }
        }

        return sequence;
    }

    // Obliczanie pochodnej wielomianu
    public static double[] differentiate(double[] coeffs) {
        double[] derivative = new double[coeffs.length - 1];
        for (int i = 0; i < derivative.length; i++) {
            derivative[i] = coeffs[i] * (coeffs.length - 1 - i);
        }
        return derivative;
    }

    // Obliczanie reszty wielomianów
    public static double[] polynomialRemainder(double[] dividend, double[] divisor) {
        double[] result = dividend.clone();
        int degreeDiff = result.length - divisor.length;

        while (degreeDiff > 0) {
            double factor = result[0] / divisor[0];
            for (int i = 0; i < divisor.length; i++) {
                result[i] -= divisor[i] * factor;
            }

            result = trimLeadingZeros(shiftLeft(result));
            degreeDiff = result.length - divisor.length;
        }

        return trimLeadingZeros(result);
    }

    // Przesunięcie elementów w tablicy w lewo
    public static double[] shiftLeft(double[] arr) {
        if (arr.length <= 1) {
            return new double[]{0};
        }
        double[] shifted = new double[arr.length - 1];
        System.arraycopy(arr, 1, shifted, 0, shifted.length);
        return shifted;
    }

    // Usuwanie wiodących zer
    public static double[] trimLeadingZeros(double[] arr) {
        int start = 0;
        while (start < arr.length && Math.abs(arr[start]) < 1e-10) {
            start++;
        }
        if (start == arr.length) {
            return new double[]{0};
        }
        double[] trimmed = new double[arr.length - start];
        System.arraycopy(arr, start, trimmed, 0, trimmed.length);
        return trimmed;
    }

    // Liczenie liczby zmian znaku w ciągu Sturma
    public static int signChanges(List<double[]> sturmSeq, double x) {
        int changes = 0;
        Double prevSign = null;

        for (double[] poly : sturmSeq) {
            double value = evaluatePolynomial(poly, x);
            double sign = Math.signum(value);
            if (prevSign != null && sign != prevSign && sign != 0) {
                changes++;
            }
            prevSign = sign;
        }
        return changes;
    }

    // Liczba pierwiastków w przedziale
    public static int countRootsSturm(List<double[]> sturmSeq, double a, double b) {
        return signChanges(sturmSeq, a) - signChanges(sturmSeq, b);
    }

    public List<double[]> invervals(double[] coeffs){
        double[] bounds = cauchyBounds(coeffs);
        double lower = bounds[0];
        double upper = bounds[1];

        List<double[]> sturmSeq = sturmSequence(coeffs);

        // Znajdowanie przedziałów z pierwiastkami
        List<double[]> intervals = new ArrayList<>();
        double step = 0.01;
        for (double a = lower; a < upper; a += step) {
            double b = a + step;
            if (countRootsSturm(sturmSeq, a, b) > 0) {
                intervals.add(new double[]{a, b});
            }
        }
        return intervals;
    }

    public static List<Double> sturmSeqRootsEvaluation(
            List<double[]> intervals,
            double[] coeffs,
            BiFunction<double[], double[], Double> rootFindingMethod
    ) {
        List<Double> roots = new ArrayList<>();
        for (double[] interval : intervals) {
            double a = interval[0];
            double b = interval[1];
            if (evaluatePolynomial(coeffs, a) * evaluatePolynomial(coeffs, b) < 0) {
                roots.add(rootFindingMethod.apply(coeffs, new double[]{a, b}));
            }
        }
        return roots;
    }

    public static double bisectionRoot(double[] coeffs, double a, double b, double tol) {
        double mid = 0;
        while (b - a > tol) {
            mid = (a + b) / 2;
            if (evaluatePolynomial(coeffs, a) * evaluatePolynomial(coeffs, mid) < 0) {
                b = mid;
            } else {
                a = mid;
            }
        }
        return mid;
    }

    public static void main(String[] args) {
        double[] coeffs = {1, 0, -9, 3, 27, -18, -31, 54, -53, 24};

        double[] bounds = cauchyBounds(coeffs);
        double lower = bounds[0];
        double upper = bounds[1];

        List<double[]> sturmSeq = sturmSequence(coeffs);

        // Znajdowanie przedziałów z pierwiastkami
        List<double[]> intervals = new ArrayList<>();
        double step = 0.01;
        for (double a = lower; a < upper; a += step) {
            double b = a + step;
            if (countRootsSturm(sturmSeq, a, b) > 0) {
                intervals.add(new double[]{a, b});
            }
        }

//         Znajdowanie pierwiastków metodą bisekcji
//        List<Double> roots = new ArrayList<>();
//        for (double[] interval : intervals) {
//            double a = interval[0];
//            double b = interval[1];
//            if (evaluatePolynomial(coeffs, a) * evaluatePolynomial(coeffs, b) < 0) {
//                roots.add(bisectionRoot(coeffs, a, b, 1e-6));
//            }
//        }

        List<Double> roots = SturmSequence.sturmSeqRootsEvaluation(
                intervals,
                coeffs,
                (poly, range) -> SturmSequence.bisectionRoot(poly, range[0], range[1], 1e-6)
        );

        // Wyświetlanie wyników
        System.out.println("Znalezione pierwiastki: " + roots);
    }
}
