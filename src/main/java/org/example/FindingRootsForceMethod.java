package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FindingRootsForceMethod {

    BigDecimal[] values;
    BigDecimal epsilon;
    BigDecimal step;

    public FindingRootsForceMethod(BigDecimal[] values, BigDecimal epsilon, BigDecimal step) {
        this.values = values;
        this.epsilon = epsilon;
        this.step = step;
    }

    public List<BigDecimal> findRootsMultiThreading(BigDecimal epsilon, BigDecimal step) throws InterruptedException, ExecutionException {
        BigDecimal start = values[9].abs().negate().add(new BigDecimal("1"));
        BigDecimal end = values[9].abs().subtract(new BigDecimal("-1"));

        int numThreads = Runtime.getRuntime().availableProcessors(); // Liczba wątków
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Callable<List<BigDecimal>>> tasks = new ArrayList<>();

        // Podziel zakres na fragmenty
        BigDecimal rangeSize = end.subtract(start).divide(BigDecimal.valueOf(numThreads));
        for (BigDecimal i = start; i.compareTo(end) < 0; i = i.add(rangeSize)) {
            BigDecimal rangeEnd = i.add(rangeSize).min(end);

            BigDecimal finalI = i;
            BigDecimal finalRangeEnd = rangeEnd;

            tasks.add(() -> {
                List<BigDecimal> localRoots = new ArrayList<>();
                for (BigDecimal x = finalI; x.compareTo(finalRangeEnd) <= 0; x = x.add(step)) {
                    BigDecimal result = evaluatePolynomial(x);
                    if (result.abs().compareTo(epsilon) == -1) {
                        localRoots.add(x);
                    }
                }
                return localRoots;
            });
        }

        // Uruchom zadania równolegle
        List<Future<List<BigDecimal>>> futures = executor.invokeAll(tasks);

        // Zbieranie wyników
        List<BigDecimal> roots = Collections.synchronizedList(new ArrayList<>());
        for (Future<List<BigDecimal>> future : futures) {
            roots.addAll(future.get());
        }

        executor.shutdown();

        List<BigDecimal> uniqueRoots = getUniqueRoots(epsilon, roots);

        return uniqueRoots;
    }

    // Funkcja obliczająca wartość wielomianu
    public BigDecimal evaluatePolynomial(BigDecimal x) {
        return values[0].multiply(x.pow(9))
                .add(values[1].multiply(x.pow(8)))
                .add(values[2].multiply(x.pow(7)))
                .add(values[3].multiply(x.pow(6)))
                .add(values[4].multiply(x.pow(5)))
                .add(values[5].multiply(x.pow(4)))
                .add(values[6].multiply(x.pow(3)))
                .add(values[7].multiply(x.pow(2)))
                .add(values[8].multiply(x))
                .add(values[9]);
    }

    // Funkcja wybierająca środek grupy
    private static BigDecimal getMiddleOfGroup(List<BigDecimal> group) {
        int middleIndex = group.size() / 2;
        return group.get(middleIndex);
    }


    public List<BigDecimal> findRootsByIterations() {
        List<BigDecimal> result;
        try {
            result = findRootsMultiThreading(this.step, this.epsilon);
//            result = findRootsMultiThreading(new BigDecimal("0.02"), new BigDecimal("0.00001"));
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    private static List<BigDecimal> getUniqueRoots(BigDecimal epsilon, List<BigDecimal> roots) {
        // Posortuj pierwiastki
        List<BigDecimal> sortedRoots = roots.stream()
                .sorted()
                .collect(Collectors.toList());

        // Lista wynikowa
        List<BigDecimal> uniqueRoots = new ArrayList<>();

        // Tymczasowa grupa
        List<BigDecimal> currentGroup = new ArrayList<>();

        for (int i = 0; i < sortedRoots.size(); i++) {
            if (currentGroup.isEmpty() || sortedRoots.get(i).subtract(currentGroup.get(currentGroup.size() - 1)).abs().compareTo(epsilon) <= 0) {
                // Dodaj do bieżącej grupy, jeśli różnica <= tolerancja
                currentGroup.add(sortedRoots.get(i));
            } else {
                // Wybierz środek bieżącej grupy i dodaj do wyników
                uniqueRoots.add(getMiddleOfGroup(currentGroup));
                // Rozpocznij nową grupę
                currentGroup.clear();
                currentGroup.add(sortedRoots.get(i));
            }
        }

        // Dodaj środek ostatniej grupy
        if (!currentGroup.isEmpty()) {
            uniqueRoots.add(getMiddleOfGroup(currentGroup));
        }
        return uniqueRoots;
    }

    private boolean isPositive(BigDecimal value) {
        return evaluatePolynomial(value).compareTo(BigDecimal.ZERO) >= 0;
    }

    private BigDecimal getMidpoint(BigDecimal a, BigDecimal b) {
        return a.add(b).divide(new BigDecimal("2"));
    }

    public List<Boolean> isFunctionPositiveInInterval(List<BigDecimal> roots){
        List<Boolean> signs = new ArrayList<>();

        if (roots.isEmpty()) {
            // Jeśli brak pierwiastków, funkcja jest dodatnia lub ujemna na całej dziedzinie.
            BigDecimal testPoint = BigDecimal.ZERO; // Można przyjąć dowolny punkt
            signs.add(isPositive(testPoint));
            return signs;
        }

        BigDecimal minusInfinityPoint = roots.get(0).subtract(BigDecimal.ONE);
        signs.add(isPositive(minusInfinityPoint));

        for (int i =0; i < roots.size()-1; i++){
            BigDecimal midPoint = getMidpoint(roots.get(i), roots.get(i+1));
            signs.add(isPositive(midPoint));
        }

        BigDecimal plusInfinityPoint = roots.get(roots.size() - 1).add(BigDecimal.ONE);
        signs.add(isPositive(plusInfinityPoint));

        return signs;
    }
}
