package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

public class PolynomialDegree9Solver extends JFrame implements ActionListener {

    private final JButton bEvalute, bReset, loadHistory;
    private final JLabel displayResult;
    private final JTextField[] fields = new JTextField[11];
    private final CartesianPlane plane;
    private final String FILE_NAME = "data_history.json";
    private final DataHistoryService dataService;

    public PolynomialDegree9Solver() {
        setSize(1500,1000);
        setTitle("Polynomial Solver Degree 9");
        setLayout(null);
        dataService = new DataHistoryService();

        int startX = 50;
        int startY = 0;
        int fieldWidth = 100;
        int fieldHeight = 50;
        int xOffset = 110;
        int positionCounter = 0;

        for (int i = fields.length-1; i >= 0; i--){
            if (i == 1){
                JLabel label = new JLabel("c");
                JTextField textField = new JTextField();

                int positionX = startX + xOffset*(fields.length-2);

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
                fields[i] = textField;

            } else if (i == 0){
                JLabel label = new JLabel("val");
                JTextField textField = new JTextField();

                int positionX = startX + xOffset*(fields.length-1);

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
                fields[i] = textField;
            } else {
                int num = i -1;
                JLabel label = new JLabel("x" + num);
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

        }

        bEvalute = new JButton("Calculate");
        bEvalute.setBounds(50,110,100,50);
        add(bEvalute);
        bEvalute.addActionListener(this);

        bReset = new JButton("Reset");
        bReset.setBounds(160,110,100,50);
        add(bReset);
        bReset.addActionListener(this);

        loadHistory = new JButton("Load previous values");
        loadHistory.setBounds(270,110,200,50);
        add(loadHistory);
        loadHistory.addActionListener(this);

        displayResult = new JLabel("Result:");
        displayResult.setBounds(50, 170, 200, 20);
        displayResult.setForeground(Color.BLUE);
        displayResult.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(displayResult);

        plane = new CartesianPlane();
        plane.setBounds(50, 200, 700, 700);
        plane.setBorder(BorderFactory.createLineBorder(Color.black));
        add(plane);
    }

    public List<BigDecimal> findRoots(List<BigDecimal> values, BigDecimal epsilon){
        List<BigDecimal> roots = new ArrayList<>();

        BigDecimal start = values.get(9).abs().negate().add(new BigDecimal("1"));
        BigDecimal end = values.get(9).abs().subtract(new BigDecimal("-1"));
        BigDecimal step = new BigDecimal("0.00001");  // Krok iteracji

        for(BigDecimal i = start; i.compareTo(end) <= 0; i = i.add(step)){
            System.out.println("start: " + i);
            BigDecimal result = evaluatePolynomial(values, i.setScale(10, RoundingMode.HALF_UP));
            if (result.abs().compareTo(epsilon) == -1) {
                roots.add(i);
            }
        }
        return roots;
    }

    public List<Double> listOfParsedDoubleValues(){
        List<Double> values = new ArrayList<>();
        for (int i = fields.length-1; i >= 0; i--){
            values.add(Double.parseDouble(fields[i].getText()));
        }
        return values;
    }

    public List<BigDecimal> listOfParsedBigDecmialValues(){
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bEvalute){

            try {
                List<BigDecimal> data = new ArrayList<>();
                for (JTextField field : fields) {
                    data.add(new BigDecimal(field.getText()));
                }
                dataService.zapisz(data, FILE_NAME);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PolynomialDegree9Solver.this, "Invalid input. Please enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(PolynomialDegree9Solver.this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            List<BigDecimal> listOfBigDecimalValues = listOfParsedBigDecmialValues();
            System.out.println(findRoots(listOfBigDecimalValues, new BigDecimal("0.02")));

            System.out.println(evaluatePolynomial(listOfBigDecimalValues, new BigDecimal(fields[0].getText())));

        }
        else if (e.getSource() == bReset){
            resetValues();
        }

        else if (e.getSource() == loadHistory){
            try {
                List<List<BigDecimal>> history = dataService.zaladuj(FILE_NAME);
                if (!history.isEmpty()) {
                    showHistoryDialog(history);
                } else {
                    JOptionPane.showMessageDialog(PolynomialDegree9Solver.this, "No data to load.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(PolynomialDegree9Solver.this, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showHistoryDialog(List<List<BigDecimal>> history) {
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
                options.get(0)
        );

        // Wybór zestawu
        if (selected != null) {
            int selectedIndex = options.indexOf(selected);
            List<BigDecimal> chosenSet = history.get(selectedIndex);

            // Wypełniamy pola tekstowe danymi z wybranego zestawu
            for (int i = 0; i < Math.min(fields.length, chosenSet.size()); i++) {
                fields[i].setText(chosenSet.get(i).toString());
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
    public static BigDecimal evaluatePolynomial(List<BigDecimal> values, BigDecimal x) {
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
    public static BigDecimal evaluateDerivative(List<BigDecimal> values, BigDecimal x) {
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
}
