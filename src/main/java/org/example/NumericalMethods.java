package org.example;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.example.utils.utils.evaluatePolynomial;

public class NumericalMethods {

    // Znajdowanie pierwiastka metodą bisekcji
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

    public static BigDecimal brentMethod(Function function, BigDecimal a, BigDecimal b, BigDecimal tolerance, int maxIterations) {
        BigDecimal fa = function.evaluate(a);
        BigDecimal fb = function.evaluate(b);

        if (fa.multiply(fb).compareTo(BigDecimal.ZERO) >= 0) {
            throw new IllegalArgumentException("Funkcja musi zmieniać znak w przedziale [a, b]");
        }

        BigDecimal c = a, fc = fa;
        BigDecimal d, e = BigDecimal.ZERO;

        for (int i = 0; i < maxIterations; i++) {
            if (fb.abs().compareTo(fc.abs()) > 0) {
                BigDecimal temp = b;
                b = c;
                c = temp;

                BigDecimal tempF = fb;
                fb = fc;
                fc = tempF;
            }

            BigDecimal tol = tolerance.multiply(BigDecimal.valueOf(0.5));
            BigDecimal m = c.subtract(b).multiply(BigDecimal.valueOf(0.5));

            if (fb.abs().compareTo(tol) < 0 || m.abs().compareTo(tol) <= 0) {
                return b;
            }

            if (e.abs().compareTo(tol) > 0 && fb.compareTo(fc) != 0) {
                // Metoda siecznych
                d = b.subtract(c).multiply(fb).divide(fc.subtract(fb), BigDecimal.ROUND_HALF_EVEN);
            } else {
                // Bisekcja
                d = m;
            }

            if (d.abs().compareTo(tol) <= 0) {
                d = (m.compareTo(BigDecimal.ZERO) > 0) ? tol : tol.negate();
            }

            a = b;
            fa = fb;

            b = b.add(d);
            fb = function.evaluate(b);

            if (fa.multiply(fb).compareTo(BigDecimal.ZERO) < 0) {
                c = a;
                fc = fa;
            }
        }

        throw new ArithmeticException("Metoda nie znalazła pierwiastka w określonej liczbie iteracji");
    }

    public static BigDecimal newtonMethod(Function function, BigDecimal x0, BigDecimal tolerance, int maxIterations) {
        BigDecimal x = x0;

        for (int i = 0; i < maxIterations; i++) {
            BigDecimal fx = function.evaluate(x);
            BigDecimal fPrimeX = function.derivative(x);

            // Sprawdzenie, czy pochodna nie jest bliska zeru
            if (fPrimeX.abs().compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Pochodna funkcji wynosi zero. Metoda Newtona nie działa.");
            }

            // Obliczenie kolejnego przybliżenia
            BigDecimal nextX = x.subtract(fx.divide(fPrimeX, MathContext.DECIMAL128));

            // Sprawdzenie zbieżności
            if (nextX.subtract(x).abs().compareTo(tolerance) < 0) {
                return nextX;
            }

            x = nextX;
        }

        throw new ArithmeticException("Nie znaleziono pierwiastka w maksymalnej liczbie iteracji.");
    }

    public static BigDecimal secantMethod(Function function, BigDecimal x0, BigDecimal x1, BigDecimal tolerance, int maxIterations) {
        BigDecimal f0 = function.evaluate(x0);
        BigDecimal f1 = function.evaluate(x1);

        for (int i = 0; i < maxIterations; i++) {
            if (f1.subtract(f0).abs().compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Dzielenie przez zero w metodzie siecznych.");
            }

            // Obliczenie nowego punktu
            BigDecimal x2 = x1.subtract(f1.multiply(x1.subtract(x0)).divide(f1.subtract(f0), MathContext.DECIMAL128));
            BigDecimal f2 = function.evaluate(x2);

            // Sprawdzenie zbieżności
            if (f2.abs().compareTo(tolerance) < 0) {
                return x2;
            }

            // Aktualizacja punktów
            x0 = x1;
            f0 = f1;
            x1 = x2;
            f1 = f2;
        }

        throw new ArithmeticException("Nie znaleziono rozwiązania w maksymalnej liczbie iteracji.");
    }

    @FunctionalInterface
    public interface Function {
        BigDecimal evaluate(BigDecimal x);

        // Domyślna implementacja dla pochodnej (można nadpisać w razie potrzeby)
        default BigDecimal derivative(BigDecimal x) {
            throw new UnsupportedOperationException("Pochodna nie została zaimplementowana.");
        }
    }
}
    

