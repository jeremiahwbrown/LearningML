import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

public class GraphPlotter {
        public static class GraphPanel extends JPanel {
            double[][] xDatasets;
            double[][] yDatasets;
            double xMin, xMax, yMin, yMax;
            double xScale, yScale;

            public GraphPanel(double[][] xPoints, double[][] yPoints) {
                if (xPoints.length != yPoints.length) {
                    throw new IllegalArgumentException("Mismatched datasets");
                }
                this.xDatasets = xPoints;
                this.yDatasets = yPoints;

                xMin = Arrays.stream(xDatasets).flatMapToDouble(Arrays::stream).min().getAsDouble();
                xMax = Arrays.stream(xDatasets).flatMapToDouble(Arrays::stream).max().getAsDouble();
                yMin = Arrays.stream(yDatasets).flatMapToDouble(Arrays::stream).min().getAsDouble();
                yMax = Arrays.stream(yDatasets).flatMapToDouble(Arrays::stream).max().getAsDouble();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int padding = 50;
                int graphWidth = getWidth() - 2 * padding;
                int graphHeight = getHeight() - 2 * padding;

                xScale = graphWidth / (xMax - xMin);
                yScale = graphHeight / (yMax - yMin);

                drawAxis(g, padding, graphWidth, graphHeight);

                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE};  // Add more colors if needed

                for (int dataset = 0; dataset < xDatasets.length; dataset++) {
                    g.setColor(colors[dataset % colors.length]);
                    double[] xPoints = xDatasets[dataset];
                    double[] yPoints = yDatasets[dataset];
                    for (int i = 0; i < xPoints.length; i++) {
                        int x = padding + (int) ((xPoints[i] - xMin) * xScale);
                        int y = getHeight() - padding - (int) ((yPoints[i] - yMin) * yScale);
                        g.fillOval(x - 3, y - 3, 6, 6);
                        if (i < xPoints.length - 1) {
                            int nextX = padding + (int) ((xPoints[i + 1] - xMin) * xScale);
                            int nextY = getHeight() - padding - (int) ((yPoints[i + 1] - yMin) * yScale);
                            g.drawLine(x, y, nextX, nextY);
                        }
                    }
                }
            }

            private void drawAxis(Graphics g, int padding, int graphWidth, int graphHeight) {
                g.drawLine(padding, getHeight() - padding, padding + graphWidth, getHeight() - padding);  // X-axis
                g.drawLine(padding, getHeight() - padding, padding, padding);  // Y-axis

                // X and Y axis labels
                for (int i = 0; i <= 10; i++) {
                    int x = padding + (i * graphWidth) / 10;
                    int y = getHeight() - padding + (i * graphHeight) / 10;
                    g.drawString(String.format("%.2f", xMin + (i * (xMax - xMin) / 10.0)), x - 15, getHeight() - padding / 2);
                    g.drawString(String.format("%.2f", yMax - (i * (yMax - yMin) / 10.0)), padding / 4, y);
                }
            }
        }
    }


