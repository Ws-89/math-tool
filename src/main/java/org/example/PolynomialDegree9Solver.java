package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class PolynomialDegree9Solver extends JFrame {

    private final JButton bEvalute, bReset, loadHistory;
    private final JTextField[] fields = new JTextField[11];
    private final CartesianPlane plane;
    private final String FILE_NAME = "data_history.json";
    private final DataManagementService dataService;
    private JTextArea resultTextArea;
    private Map<String, List<BigDecimal>> results;
    private Map<String, List<Boolean>> signs;
    private FindingRootsForceMethod rootsForceMethod;

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
            plane.setCoefficients(arrayOfParsedBigDecimalValues());
            rootsForceMethod = new FindingRootsForceMethod(arrayOfParsedBigDecimalValues(), new BigDecimal("0.00001"),new BigDecimal("0.02"));
            List<BigDecimal> byIteration = rootsForceMethod.findRootsByIterations();
            results.put("byIteration", byIteration);
            List<Boolean> byIterationSigns = rootsForceMethod.isFunctionPositiveInInterval(byIteration);
            signs.put("byIteration", byIterationSigns);
            setResultTextArea(results, signs, rootsForceMethod.evaluatePolynomial(new BigDecimal(fields[0].getText())));
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

    public BigDecimal[] arrayOfParsedBigDecimalValues(){
        BigDecimal[] values = new BigDecimal[fields.length];
        int counter = 0;
        for (int i = fields.length-1; i >= 0; i--){
            values[counter++] = new BigDecimal(fields[i].getText());
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



}
