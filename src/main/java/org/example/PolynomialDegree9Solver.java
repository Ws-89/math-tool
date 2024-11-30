package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

public class PolynomialDegree9Solver extends JFrame implements ActionListener {

    JButton bEvalute, bReset;
    JLabel displayResult, x9Label, x8Label, x7Label, x6Label, x5Label, x4Label, x3Label, x2Label, x1Label, cLabel, xValLabel;
    JTextField x9TextField, x8TextField, x7TextField, x6TextField, x5TextField, x4TextField, x3TextField, x2TextField, x1TextField, cTextField, xValTextField;
    CartesianPlane plane;

    public PolynomialDegree9Solver() {
        setSize(1500,1000);
        setTitle("Polynomial Solver Degree 9");
        setLayout(null);

        x9Label = new JLabel("x9");
        x9Label.setBounds(50, 0, 100, 50);
        add(x9Label);
        x9TextField = new JTextField();
        x9TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x9TextField::selectAll);
            }
        });
        x9TextField.setText("0");
        x9TextField.setBounds(50,50,100,50);
        add(x9TextField);

        x8Label = new JLabel("x8");
        x8Label.setBounds(160, 0, 100, 50);
        add(x8Label);
        x8TextField = new JTextField();
        x8TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x8TextField::selectAll);
            }
        });
        x8TextField.setText("0");
        x8TextField.setBounds(160,50,100,50);
        add(x8TextField);

        x7Label = new JLabel("x7");
        x7Label.setBounds(270, 0, 100, 50);
        add(x7Label);
        x7TextField = new JTextField();
        x7TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x7TextField::selectAll);
            }
        });
        x7TextField.setText("0");
        x7TextField.setBounds(270,50,100,50);
        add(x7TextField);

        x6Label = new JLabel("x6");
        x6Label.setBounds(380, 0, 100, 50);
        add(x6Label);
        x6TextField = new JTextField();
        x6TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x6TextField::selectAll);
            }
        });
        x6TextField.setText("0");
        x6TextField.setBounds(380,50,100,50);
        add(x6TextField);

        x5Label = new JLabel("x5");
        x5Label.setBounds(490, 0, 100, 50);
        add(x5Label);
        x5TextField = new JTextField();
        x5TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x5TextField::selectAll);
            }
        });
        x5TextField.setText("0");
        x5TextField.setBounds(490,50,100,50);
        add(x5TextField);

        x4Label = new JLabel("x4");
        x4Label.setBounds(600, 0, 100, 50);
        add(x4Label);
        x4TextField = new JTextField();
        x4TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x4TextField::selectAll);
            }
        });
        x4TextField.setText("0");
        x4TextField.setBounds(600,50,100,50);
        add(x4TextField);

        x3Label = new JLabel("x3");
        x3Label.setBounds(710, 0, 100, 50);
        add(x3Label);
        x3TextField = new JTextField();
        x3TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x3TextField::selectAll);
            }
        });
        x3TextField.setText("0");
        x3TextField.setBounds(710,50,100,50);
        add(x3TextField);

        x2Label = new JLabel("x2");
        x2Label.setBounds(820, 0, 100, 50);
        add(x2Label);
        x2TextField = new JTextField();
        x2TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x2TextField::selectAll);
            }
        });
        x2TextField.setText("0");
        x2TextField.setBounds(820,50,100,50);
        add(x2TextField);

        x1Label = new JLabel("x1");
        x1Label.setBounds(930, 0, 100, 50);
        add(x1Label);
        x1TextField = new JTextField();
        x1TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(x1TextField::selectAll);
            }
        });
        x1TextField.setText("0");
        x1TextField.setBounds(930,50,100,50);
        add(x1TextField);

        cLabel = new JLabel("c");
        cLabel.setBounds(1040, 0, 100, 50);
        add(cLabel);
        cTextField = new JTextField();
        cTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(cTextField::selectAll);
            }
        });
        cTextField.setText("0");
        cTextField.setBounds(1040,50,100,50);
        add(cTextField);

        xValLabel = new JLabel("X val");
        xValLabel.setBounds(1150, 0, 100, 50);
        add(xValLabel);
        xValTextField = new JTextField();
        xValTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(xValTextField::selectAll);
            }
        });
        xValTextField.setText("0");
        xValTextField.setBounds(1150,50,100,50);
        add(xValTextField);

        bEvalute = new JButton("Calculate");
        bEvalute.setBounds(50,110,100,50);
        add(bEvalute);
        bEvalute.addActionListener(this);

        bReset = new JButton("Reset");
        bReset.setBounds(160,110,100,50);
        add(bReset);
        bReset.addActionListener(this);

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

        BigDecimal start = new BigDecimal(cTextField.getText()).abs().negate().add(new BigDecimal("1"));
        BigDecimal end = new BigDecimal(cTextField.getText()).abs().subtract(new BigDecimal("-1"));
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
        List<Double> values = Arrays.asList(
                Double.parseDouble(x9TextField.getText()),
                Double.parseDouble(x8TextField.getText()),
                Double.parseDouble(x7TextField.getText()),
                Double.parseDouble(x6TextField.getText()),
                Double.parseDouble(x5TextField.getText()),
                Double.parseDouble(x4TextField.getText()),
                Double.parseDouble(x3TextField.getText()),
                Double.parseDouble(x2TextField.getText()),
                Double.parseDouble(x1TextField.getText()),
                Double.parseDouble(cTextField.getText()),
                Double.parseDouble(xValTextField.getText()));

        return values;
    }

    public List<BigDecimal> listOfParsedBigDecmialValues(){
        List<BigDecimal> values = Arrays.asList(
                new BigDecimal(x9TextField.getText()),
                new BigDecimal(x8TextField.getText()),
                new BigDecimal(x7TextField.getText()),
                new BigDecimal(x6TextField.getText()),
                new BigDecimal(x5TextField.getText()),
                new BigDecimal(x4TextField.getText()),
                new BigDecimal(x3TextField.getText()),
                new BigDecimal(x2TextField.getText()),
                new BigDecimal(x1TextField.getText()),
                new BigDecimal(cTextField.getText()),
                new BigDecimal(xValTextField.getText()));
        return values;
    }

    public Map<String, Double> parsedValuesMap(){
        Map<String, Double> values = new HashMap<String, Double>();

        values.put("x9", Double.parseDouble(x9TextField.getText()));
        values.put("x8", Double.parseDouble(x8TextField.getText()));
        values.put("x7", Double.parseDouble(x7TextField.getText()));
        values.put("x6", Double.parseDouble(x6TextField.getText()));
        values.put("x5", Double.parseDouble(x5TextField.getText()));
        values.put("x4", Double.parseDouble(x4TextField.getText()));
        values.put("x3", Double.parseDouble(x3TextField.getText()));
        values.put("x2", Double.parseDouble(x2TextField.getText()));
        values.put("x1", Double.parseDouble(x1TextField.getText()));
        values.put("c", Double.parseDouble(cTextField.getText()));
        values.put("xVal", Double.parseDouble(xValTextField.getText()));

        return values;
    }

    public void resetValues(){
        x9TextField.setText("0");
        x8TextField.setText("0");
        x7TextField.setText("0");
        x6TextField.setText("0");
        x5TextField.setText("0");
        x4TextField.setText("0");
        x3TextField.setText("0");
        x2TextField.setText("0");
        x1TextField.setText("0");
        cTextField.setText("0");
        xValTextField.setText("0");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bEvalute){
            List<BigDecimal> listOfBigDecimalValues = listOfParsedBigDecmialValues();
            System.out.println(findRoots(listOfBigDecimalValues, new BigDecimal("0.02")));

            System.out.println(evaluatePolynomial(listOfBigDecimalValues, new BigDecimal(xValTextField.getText())));

        }
        else if (e.getSource() == bReset){
            resetValues();
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
