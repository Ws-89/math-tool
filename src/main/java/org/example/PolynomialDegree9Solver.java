package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class PolynomialDegree9Solver extends JFrame {

    private final JButton bEvalute, bReset, loadHistory;
    private final JTextField[] fields = new JTextField[11];
    private final CartesianPlane plane;
    private final String FILE_NAME = "data_history.json";
    private final DataManagementService dataService;
    private JTextArea resultTextArea;
    private Map<String, List<BigDecimal>> results;
    private Map<String, List<Boolean>> signs;

    public PolynomialDegree9Solver() {
        setSize(1500,1000);
        setTitle("Polynomial Solver Degree 9");
        setLayout(null);
        dataService = new DataManagementService();
        results = new HashMap<>();
        signs = new HashMap<>();

        int startX = 50;
        int startY = 0;
        int fieldWidth = 100;
        int fieldHeight = 50;
        int xOffset = 110;
        int positionCounter = 0;

        for (int i = fields.length-1; i >= 0; i--){
            JLabel label;
            if (i == 1) {
                label = new JLabel("c");
            } else if (i == 0){
                    label = new JLabel("val");
            } else {
                int num = i -1;
                label = new JLabel("x" + num);
            }

            JTextField textField = new JTextField();

            int positionX = startX + xOffset * positionCounter;

            label.setBounds(positionX, startY, fieldWidth, fieldHeight);
            textField.setBounds(positionX, startY + 50, fieldWidth, fieldHeight);
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    SwingUtilities.invokeLater(textField::selectAll);
                }
            });
            textField.setText("0");
            add(textField);
            add(label);
            positionCounter++;
            fields[i] = textField;
        }

        bReset = new JButton("Reset");
        bReset.setBounds(160,110,100,50);
        add(bReset);
        bReset.addActionListener(e -> resetValues());

        loadHistory = new JButton("Load previous values");
        loadHistory.setBounds(270,110,200,50);
        add(loadHistory);
        loadHistory.addActionListener(e -> loadHistoryData());

        plane = new CartesianPlane();
        plane.setBounds(50, 200, 700, 700);
        plane.setBorder(BorderFactory.createLineBorder(Color.black));
        add(plane);

        bEvalute = new JButton("Calculate");
        bEvalute.setBounds(50,110,100,50);
        add(bEvalute);
        bEvalute.addActionListener(e -> {
            savePolynomialCoefficients();
            plane.setCoefficients(listOfParsedBigDecimalValues());
            List<BigDecimal> byIteration = findRootsByIterations();
            results.put("byIteration", byIteration);
            List<Boolean> byIterationSigns = isFunctionPositiveInInterval(byIteration);
            signs.put("byIteration", byIterationSigns);
            setResultTextArea(results, signs, evaluatePolynomial(listOfParsedBigDecimalValues(), new BigDecimal(fields[0].getText())));
        });

        // Pole tekstowe do wyświetlania wyników
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false); // Wyłączenie edycji
        resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Matematyczny styl czcionki
        resultTextArea.setBorder(new EmptyBorder(10, 10, 10, 10)); // Marginesy wewnętrzne
        resultTextArea.setBackground(new Color(245, 245, 245)); // Jasnoszare tło dla czytelności

        // Dodanie paska przewijania
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        // Dodanie elementów do ramki
        this.getContentPane().add(scrollPane);
        scrollPane.setBounds(800, 200, 600, 700);

    }

    private boolean isPositive(BigDecimal value) {
        return evaluatePolynomial(listOfParsedBigDecimalValues(), value).compareTo(BigDecimal.ZERO) >= 0;
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

    public void setResultTextArea(Map<String, List<BigDecimal>> results, Map<String, List<Boolean>> signs, BigDecimal eval_result) {
        // Initialize StringBuilder to construct the text
        StringBuilder content = new StringBuilder();

        // Add header with polynomial result
        content.append("------------------------\n");
        content.append("The polynomial result for the number ").append(fields[0].getText())
                .append(" is: ").append(eval_result).append("\n");
        content.append("------------------------\n");

        // Iterate over the methods' results
        for (Map.Entry<String, List<BigDecimal>> entry : results.entrySet()) {
            String methodName = entry.getKey();
            List<BigDecimal> roots = entry.getValue();
            List<Boolean> methodSigns = signs.get(methodName);

            content.append("Method name: ").append(methodName).append("\n");

            // Add intervals with function values
            if (roots.size() > 0) {
                content.append("For the interval (-∞, ").append(roots.get(0)).append(") the function has a value: ")
                        .append(methodSigns.get(0) ? "positive" : "negative").append("\n");

                for (int i = 1; i < roots.size(); i++) {
                    content.append("For the interval (").append(roots.get(i - 1)).append(", ").append(roots.get(i)).append(") the function has a value: ")
                            .append(methodSigns.get(i) ? "positive" : "negative").append("\n");
                }

                content.append("For the interval (").append(roots.get(roots.size() - 1)).append(", ∞) the function has a value: ")
                        .append(methodSigns.get(methodSigns.size() - 1) ? "positive" : "negative").append("\n");
            } else {
                // No roots case
                content.append("No roots, the function has a value: ")
                        .append(methodSigns.get(0) ? "positive" : "negative").append(" over the entire interval\n");
            }

            content.append("------------------------\n");
        }

        // Set the text in the text area
        resultTextArea.setText(content.toString());
    }


    public List<BigDecimal> findRootsMultiThreading(List<BigDecimal> values, BigDecimal epsilon, BigDecimal step) throws InterruptedException, ExecutionException {
        BigDecimal start = values.get(9).abs().negate().add(new BigDecimal("1"));
        BigDecimal end = values.get(9).abs().subtract(new BigDecimal("-1"));

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
                    BigDecimal result = evaluatePolynomial(values, x);
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

    public List<Double> listOfParsedDoubleValues(){
        List<Double> values = new ArrayList<>();
        for (int i = fields.length-1; i >= 0; i--){
            values.add(Double.parseDouble(fields[i].getText()));
        }
        return values;
    }

    public List<BigDecimal> listOfParsedBigDecimalValues(){
        List<BigDecimal> values = new ArrayList<>();
        for (int i = fields.length-1; i >= 0; i--){
            values.add(new BigDecimal(fields[i].getText()));
        }
        return values;
    }

    public Map<String, Double> parsedValuesMap(){
        Map<String, Double> values = new HashMap<String, Double>();
        for (int i = fields.length-1; i >= 0; i--){
            values.put(String.valueOf(i) ,Double.parseDouble(fields[i].getText()));
        }
        return values;
    }

    public void resetValues(){
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText("0");
        }
    }

    private void loadHistoryData() {
        try {
            List<List<BigDecimal>> history = dataService.load(FILE_NAME);
            if (!history.isEmpty()) {
                loadPolynomialCoefficients(history);
            } else {
                JOptionPane.showMessageDialog(PolynomialDegree9Solver.this, "No data to load.");
            }
        } catch (IOException ex) {
            showErrorMessage("Error loading data: " + ex.getMessage());
        }
    }

    private List<BigDecimal> findRootsByIterations() {
        List<BigDecimal> listOfBigDecimalValues = listOfParsedBigDecimalValues();
        List<BigDecimal> result;
        try {
            result = findRootsMultiThreading(listOfBigDecimalValues, new BigDecimal("0.02"), new BigDecimal("0.00001"));
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    private void savePolynomialCoefficients() {
        try {
            List<BigDecimal> data = listOfParsedBigDecimalValues();
            dataService.save(data, FILE_NAME);
        } catch (NumberFormatException ex) {
            showErrorMessage("Invalid input. Please enter numbers only.");
        } catch (IOException ex) {
            showErrorMessage("Error saving data: " + ex.getMessage());
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void loadPolynomialCoefficients(List<List<BigDecimal>> history) {
        // Tworzymy listę opcji
        List<String> options = new ArrayList<>();
        for (int i = 0; i < history.size(); i++) {
            options.add("Set " + (i + 1) + ": " + history.get(i).toString());
        }

        // Okno dialogowe z listą rozwijaną
        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Select a data set:",
                "Data History",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options.toArray(),
                options.get(options.size()-1)
        );

        // Wybór zestawu
        if (selected != null) {
            int selectedIndex = options.indexOf(selected);
            List<BigDecimal> chosenSet = history.get(selectedIndex);

            // Wypełniamy pola tekstowe danymi z wybranego zestawu
            for (int i = 0; i < Math.min(fields.length, chosenSet.size()); i++) {
                fields[i].setText(chosenSet.reversed().get(i).toString());
            }
        }
    }

    BigDecimal newtonRaphson(List<BigDecimal> values, BigDecimal x0, int maxIterations, BigDecimal tolerance) {
        BigDecimal x = x0;
        for (int i = 0; i < maxIterations; i++) {
            BigDecimal fx = evaluatePolynomial(values, x);
            BigDecimal dfx = evaluateDerivative(values, x);

            // Jeśli pochodna jest bliska zeru, metoda może nie działać poprawnie
            if (dfx.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Pochodna bliska zeru, metoda nie zbiega.");
            }

            // Oblicz kolejne przybliżenie
            BigDecimal xNext = x.subtract(fx.divide(dfx, MathContext.DECIMAL128));

            // Sprawdź kryterium zbieżności
            if (xNext.subtract(x).abs().compareTo(tolerance) < 0) {
                return xNext; // Znaleziono rozwiązanie
            }
            x = xNext;
        }
        throw new ArithmeticException("Nie znaleziono rozwiązania w maksymalnej liczbie iteracji.");
    }

    // Funkcja obliczająca wartość wielomianu
    public BigDecimal evaluatePolynomial(List<BigDecimal> values, BigDecimal x) {
        return values.get(0).multiply(x.pow(9))
                .add(values.get(1).multiply(x.pow(8)))
                .add(values.get(2).multiply(x.pow(7)))
                .add(values.get(3).multiply(x.pow(6)))
                .add(values.get(4).multiply(x.pow(5)))
                .add(values.get(5).multiply(x.pow(4)))
                .add(values.get(6).multiply(x.pow(3)))
                .add(values.get(7).multiply(x.pow(2)))
                .add(values.get(8).multiply(x))
                .add(values.get(9));
    }

    // Funkcja obliczająca wartość pochodnej
    public BigDecimal evaluateDerivative(List<BigDecimal> values, BigDecimal x) {
        return values.get(0).multiply(BigDecimal.valueOf(9)).multiply(x.pow(8))
                .add(values.get(1).multiply(BigDecimal.valueOf(8)).multiply(x.pow(7)))
                .add(values.get(2).multiply(BigDecimal.valueOf(7)).multiply(x.pow(6)))
                .add(values.get(3).multiply(BigDecimal.valueOf(6)).multiply(x.pow(5)))
                .add(values.get(4).multiply(BigDecimal.valueOf(5)).multiply(x.pow(4)))
                .add(values.get(5).multiply(BigDecimal.valueOf(4)).multiply(x.pow(3)))
                .add(values.get(6).multiply(BigDecimal.valueOf(3)).multiply(x.pow(2)))
                .add(values.get(7).multiply(BigDecimal.valueOf(2)).multiply(x))
                .add(values.get(8));
    }

    // Funkcja wybierająca środek grupy
    private static BigDecimal getMiddleOfGroup(List<BigDecimal> group) {
        int middleIndex = group.size() / 2;
        return group.get(middleIndex);
    }

}
