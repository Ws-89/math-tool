package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class PolynomialDegree9Solver extends JFrame implements ActionListener {

    JButton bEvalute, bReset;
    JLabel lWyswietlDate, x9Label, x8Label, x7Label, x6Label, x5Label, x4Label, x3Label, x2Label, x1Label, cLabel, xValLabel;
    JTextField x9TextField, x8TextField, x7TextField, x6TextField, x5TextField, x4TextField, x3TextField, x2TextField, x1TextField, cTextField, xValTextField;

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

        lWyswietlDate = new JLabel("Result:");
        lWyswietlDate.setBounds(50, 170, 200, 20);
        lWyswietlDate.setForeground(Color.BLUE);
        lWyswietlDate.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(lWyswietlDate);
    }

    public double calculate(double x9, double x8, double x7, double x6, double x5, double x4, double x3, double x2, double x1, double c, Double x){
        return x9*pow(x, 9) + x8*pow(x, 8) + x7*pow(x, 7) + x6*pow(x, 6) + x5*pow(x, 5) + x4*pow(x, 4) + x3*pow(x, 3) + x2*pow(x, 2) + x1*x + c;
    }

    public List<Double> findRoots(){
        List<Double> roots = new ArrayList<Double>();
        for(double i = -abs(Double.parseDouble(cTextField.getText())); i <= abs(Double.parseDouble(cTextField.getText())); i+= 0.25){
            System.out.println(i);
            if (calculate(
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
                    i) == 0){
                System.out.println();
                roots.add(i);
            }
        }
        return roots;
    }

    public String PolynomialDivider(Double num, int rank){
        Map<String, Double> polynomialValues = new HashMap<>();
        Map<String, Double> result = new HashMap<>();

        Double x9 = Double.parseDouble(x7TextField.getText());
        if (x9 != 0) {
            polynomialValues.put("x9", x9);
        }
        Double x8 = Double.parseDouble(x6TextField.getText());
        if (x8 != 0) {
            polynomialValues.put("x6", x8);
        }
        Double x7 = Double.parseDouble(x7TextField.getText());
        if (x7 != 0) {
            polynomialValues.put("x7", x7);
        }
        Double x6 = Double.parseDouble(x6TextField.getText());
        if (x6 != 0) {
            polynomialValues.put("x6", x6);
        }
        Double x5 = Double.parseDouble(x5TextField.getText());
        if (x5 != 0) {
            polynomialValues.put("x5", x5);
        }
        Double x4 = Double.parseDouble(x4TextField.getText());
        if (x4 != 0) {
            polynomialValues.put("x4", x4);
        }
        Double x3 = Double.parseDouble(x3TextField.getText());
        if (x3 != 0) {
            polynomialValues.put("x3", x3);
        }
        Double x2 = Double.parseDouble(x2TextField.getText());
        if (x2 != 0) {
            polynomialValues.put("x2", x2);
        }
        Double x1 = Double.parseDouble(x1TextField.getText());
        if (x1 != 0) {
            polynomialValues.put("x1", x1);
        }
        Double c = Double.parseDouble(cTextField.getText());
        if (c != 0) {
            polynomialValues.put("c", c);
        }



        return "0";
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
            double fun_value = calculate(
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
            lWyswietlDate.setText(String.valueOf(fun_value));
            System.out.println(findRoots());
        }
        else if (e.getSource() == bReset){
            resetValues();
        }


    }
}
