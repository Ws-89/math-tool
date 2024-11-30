package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;

public class CartesianPlane extends JPanel {
    private int offsetX = 350, offsetY = 350; // Pozycja środka siatki
    private double scale = 1.0; // Skala siatki

    private BigDecimal X9 = new BigDecimal("0.0");
    private BigDecimal X8 = new BigDecimal("0.0");
    private BigDecimal X7 = new BigDecimal("0.0");
    private BigDecimal X6 = new BigDecimal("0.0");
    private BigDecimal X5 = new BigDecimal("0.0");
    private BigDecimal X4 = new BigDecimal("0.0");
    private BigDecimal X3 = new BigDecimal("1.0");
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

        // Przekształcenie dla skali i przesunięcia
        // Ustawienie globalnej transformacji dla rysowania

        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, scale);

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
        g2d.drawString("5", 50, -5);
        g2d.drawString("-5", -50, -5);
        g2d.drawString("5", 0, -55);
        g2d.drawString("-5", 0, 45);

        // Rysowanie funkcji kwadratowej
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        BigDecimal x = START;
        BigDecimal previousX = null;
        BigDecimal previousY = null;

        double graph_scale = 10.0;

        while (x.compareTo(STOP) <= 0){
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
    }

    public void setCoefficients(BigDecimal X9,
                                BigDecimal X8,
                                BigDecimal X7,
                                BigDecimal X6,
                                BigDecimal X5,
                                BigDecimal X4,
                                BigDecimal X3,
                                BigDecimal X2,
                                BigDecimal X1,
                                BigDecimal C
                                ) {
        this.X9 = X9;
        this.X8 = X8;
        this.X7 = X7;
        this.X6 = X6;
        this.X5 = X5;
        this.X4 = X4;
        this.X3 = X3;
        this.X2 = X2;
        this.X1 = X1;
        this.C = C;
        repaint();
    }



}