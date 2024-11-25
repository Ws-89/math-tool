package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.math.BigDecimal;

public class CartesianPlane extends JPanel {
    private int offsetX = 400, offsetY = 400; // Pozycja środka siatki
    private double scale = 1.0; // Skala siatki
    private String transformedCoordinates = "";

    private BigDecimal X9 = new BigDecimal("0.0");
    private BigDecimal X8 = new BigDecimal("0.0");
    private BigDecimal X7 = new BigDecimal("0.0");
    private BigDecimal X6 = new BigDecimal("0.0");
    private BigDecimal X5 = new BigDecimal("0.0");
    private BigDecimal X4 = new BigDecimal("0.0");
    private BigDecimal X3 = new BigDecimal("0.0");
    private BigDecimal X2 = new BigDecimal("1.0");
    private BigDecimal X1 = new BigDecimal("0.0");
    private BigDecimal C = new BigDecimal("0.0");

    private BigDecimal STEP = new BigDecimal("0.1");
    private BigDecimal START = new BigDecimal("-20");
    private BigDecimal STOP = new BigDecimal("20");

    public CartesianPlane() {
        // Obsługa przesuwania siatki
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point lastPoint = null;

            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastPoint != null) {
                    int dx = e.getX() - lastPoint.x;
                    int dy = e.getY() - lastPoint.y;
                    offsetX += dx;
                    offsetY += dy;
                    lastPoint = e.getPoint();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastPoint = null;
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        // Obsługa przybliżania i oddalania
        this.addMouseWheelListener(e -> {
            if (e.getPreciseWheelRotation() < 0) {
                scale *= 1.1;
            } else if (scale > 0.001){
                scale /= 1.1;
            }
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Przekształcenie dla skali i przesunięcia
        AffineTransform transform = g2d.getTransform();
        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, scale);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Pobranie współrzędnych myszy w oknie
                Point2D mousePoint = new Point2D.Double(e.getX(), e.getY());

                // Transformacja współrzędnych do układu "przesuniętego"
                try {
                    Point2D transformedPoint = transform.inverseTransform(mousePoint, null);
                    transformedCoordinates = String.format(
                            "Współrzędne (po transformacji): X=%.2f, Y=%.2f",
                            transformedPoint.getX(), transformedPoint.getY()
                    );
                } catch (Exception ex) {
                    transformedCoordinates = "Błąd transformacji!";
                }

                // Odśwież panel
                repaint();
            }
        });


        // Rysowanie siatki
        g2d.setColor(Color.LIGHT_GRAY);
        for (int x = -1000000; x <= 1000000; x += 50) {
            g2d.drawLine(x, -1000000, x, 1000000);
        }
        for (int y = -1000000; y <= 1000000; y += 50) {
            g2d.drawLine(-1000000, y, 1000000, y);
        }

        // Rysowanie osi X i Y
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(-1000000, 0, 1000000, 0); // Oś X
        g2d.drawLine(0, -1000000, 0, 1000000); // Oś Y

        // Dodawanie skali do siatki
        g2d.setColor(Color.BLUE);
        g2d.drawString("5", 50, -10);
        g2d.drawString("-5", -50, -10);
        g2d.drawString("5", 0, -50);
        g2d.drawString("-5", 0, 50);

        // Rysowanie funkcji kwadratowej
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        BigDecimal x = START;
        BigDecimal previousX = null;
        BigDecimal previousY = null;

        double graph_scale = 10.0;

        while (x.compareTo(STOP) <= 0){
            //Convert y = ax^2 + bx + c
            BigDecimal y = X9.multiply(x.pow(9))
                    .add(X8.multiply(x.pow(8)))
                    .add(X7.multiply(x.pow(7)))
                    .add(X6.multiply(x.pow(6)))
                    .add(X5.multiply(x.pow(5)))
                    .add(X4.multiply(x.pow(4)))
                    .add(X3.multiply(x.pow(3)))
                    .add(X2.multiply(x.pow(2)))
                    .add(X1.multiply(x.pow(1)))
                    .add(C);

            if(previousX != null & previousY != null) {
                // Convert coordinates to screen space
                int x1 = (int) (previousX.doubleValue() * graph_scale);
                int y1 = (int) (previousY.doubleValue() * graph_scale);
                int x2 = (int) (x.doubleValue() * graph_scale);
                int y2 = (int) (y.doubleValue() * graph_scale);

                g2d.drawLine(x1, -y1, x2, -y2);
            }

            previousX = x;
            previousY = y;

            x = x.add(STEP);
        }

        // Przywrócenie oryginalnego przekształcenia
        g2d.setTransform(transform);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Scale: " + String.format("%.2f", scale), 10, 20);
        g2d.drawString(transformedCoordinates, 10, 40);

    }

    public void setCoefficients(double a, double b, double c) {
//        this.a = a;
//        this.b = b;
//        this.c = c;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Układ współrzędnych");
        CartesianPlane plane = new CartesianPlane();

        // Panel z polami tekstowymi
        JPanel controls = new JPanel();
        JTextField fieldA = new JTextField("1", 5);
        JTextField fieldB = new JTextField("0", 5);
        JTextField fieldC = new JTextField("0", 5);
        JButton drawButton = new JButton("Rysuj");

        controls.add(new JLabel("a:"));
        controls.add(fieldA);
        controls.add(new JLabel("b:"));
        controls.add(fieldB);
        controls.add(new JLabel("c:"));
        controls.add(fieldC);
        controls.add(drawButton);

        drawButton.addActionListener(e -> {
            try {
                double a = Double.parseDouble(fieldA.getText());
                double b = Double.parseDouble(fieldB.getText());
                double c = Double.parseDouble(fieldC.getText());
                plane.setCoefficients(a, b, c);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Wprowadź poprawne liczby!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(plane, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}