package io.github.dankoller.view;

import javax.swing.*;
import java.awt.*;

public class Vertex extends JPanel {
    private static final int VERTEX_RADIUS = 50;
    private static final Color VERTEX_COLOR = Color.WHITE;
    private static final Color VERTEX_SELECTED_COLOR = Color.YELLOW;
    private Color labelBackground = VERTEX_COLOR;
    private final String vertexLabel;

    public Vertex(String label, Color background) {
        super();
        this.vertexLabel = label;
        setName("Vertex %s".formatted(label));
        setBackground(background);
        addLabel(label);
        setSize(VERTEX_RADIUS, VERTEX_RADIUS);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(labelBackground);
        g.fillOval(0, 0, VERTEX_RADIUS, VERTEX_RADIUS);
    }

    public void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setName("VertexLabel %s".formatted(text));
        label.setForeground(getBackground());
        label.setFont(new Font("Arial", Font.BOLD, VERTEX_RADIUS * 4 / 5));
        add(label);
    }

    public void setLabelBackground(Color newColor) {
        if (newColor.equals(labelBackground)) {
            return;
        }
        labelBackground = newColor;
        paintComponent(getGraphics());
        paintChildren(getGraphics());
    }

    public void select() {
        setLabelBackground(VERTEX_SELECTED_COLOR);
    }

    public void deselect() {
        setLabelBackground(VERTEX_COLOR);
    }

    public boolean isInside(Point point) {
        return point.distance(getCenter()) <= VERTEX_RADIUS / 2.0;
    }

    public Point getCenter() {
        return new Point(getX() + VERTEX_RADIUS / 2, getY() + VERTEX_RADIUS / 2);
    }

    public static int getVertexRadius() {
        return VERTEX_RADIUS;
    }

    public static Color getVertexColor() {
        return VERTEX_COLOR;
    }
    public static Color getVertexSelectedColor() {
        return VERTEX_SELECTED_COLOR;
    }

    public String getLabel() {
        return vertexLabel;
    }
}
