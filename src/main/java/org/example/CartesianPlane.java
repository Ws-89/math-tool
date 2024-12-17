package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.util.List;

public class CartesianPlane extends JPanel {
    private int offsetX = 350, offsetY = 350; // Pozycja środka siatki
    private double scale = 1.0; // Skala siatki

    private BigDecimal[] fields = new BigDecimal[10];

    private BigDecimal STEP = new BigDecimal("0.1");
    private BigDecimal START = new BigDecimal("-200");
    private BigDecimal STOP = new BigDecimal("200");

    private boolean coefficientsSet = false;

    public CartesianPlane() {
        for (int i = 0; i < fields.length; i++) {
            fields[i] = BigDecimal.ZERO;
        }

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

        if(coefficientsSet){
            // Rysowanie funkcji kwadratowej
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));

            BigDecimal x = START;
            BigDecimal previousX = null;
            BigDecimal previousY = null;

            double graph_scale = 10.0;
            while (x.compareTo(STOP) <= 0){
                BigDecimal y = evaluatePolynomialForGraph(this.fields, x);

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
        }

        // Przywrócenie oryginalnego przekształcenia
        g2d.setTransform(transform);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Scale: " + String.format("%.2f", scale), 10, 20);
    }

    // Obliczanie wartości wielomianu w punkcie
    public BigDecimal evaluatePolynomialForGraph(BigDecimal[] coeffs, BigDecimal x) {
        BigDecimal result = BigDecimal.ZERO;
        for(int i =0; i < coeffs.length-1; i++){
            result = result.multiply(x).add(coeffs[i]);
        }
        return result;
    }

    public void setCoefficients(BigDecimal[] values){
        this.fields = values;

        this.coefficientsSet = true;
        repaint();
    }



}