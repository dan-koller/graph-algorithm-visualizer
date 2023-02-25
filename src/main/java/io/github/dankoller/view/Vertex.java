package io.github.dankoller.view;

import javax.swing.*;
import java.awt.*;

public class Vertex extends JPanel {
    private static final int VERTEX_RADIUS = 50;
    private static final Color VERTEX_COLOR = Color.WHITE;
    private static final Color VERTEX_SELECTED_COLOR = Color.YELLOW;
    private Color labelBackground = VERTEX_COLOR;
    private final String vertexLabel;

    /**
     * Creates a new Vertex with the given label and background color.
     *
     * @param label      The label of the vertex
     * @param background The background color of the vertex
     */
    public Vertex(String label, Color background) {
        super();
        this.vertexLabel = label;
        setName("Vertex %s".formatted(label));
        setBackground(background);
        addLabel(label);
        setSize(VERTEX_RADIUS, VERTEX_RADIUS);
    }

    /**
     * Creates a new Vertex with the given label and the default background color.
     *
     * @param g The graphics object to draw the vertex on
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(labelBackground);
        g.fillOval(0, 0, VERTEX_RADIUS, VERTEX_RADIUS);
    }

    /**
     * Adds a label (i.e. the vertex's name) to the vertex.
     *
     * @param text The text of the label
     */
    public void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setName("VertexLabel %s".formatted(text));
        label.setForeground(getBackground());
        label.setFont(new Font("Arial", Font.BOLD, VERTEX_RADIUS * 4 / 5));
        add(label);
    }

    /**
     * Sets the background color of the vertex.
     *
     * @param newColor The new background color
     */
    public void setLabelBackground(Color newColor) {
        if (newColor.equals(labelBackground)) {
            return;
        }
        labelBackground = newColor;
        paintComponent(getGraphics());
        paintChildren(getGraphics());
    }

    /**
     * Changes the background color if the vertex is selected.
     */
    public void select() {
        setLabelBackground(VERTEX_SELECTED_COLOR);
    }

    /**
     * Changes the background color if the vertex is deselected.
     */
    public void deselect() {
        setLabelBackground(VERTEX_COLOR);
    }

    /**
     * Checks if the given point is inside the vertex.
     *
     * @param point The point to check
     * @return True if the point is inside the vertex, false otherwise
     */
    public boolean isInside(Point point) {
        return point.distance(getCenter()) <= VERTEX_RADIUS / 2.0;
    }

    /**
     * Returns the center of the vertex.
     *
     * @return The center of the vertex
     */
    public Point getCenter() {
        return new Point(getX() + VERTEX_RADIUS / 2, getY() + VERTEX_RADIUS / 2);
    }

    /**
     * Returns the radius of the vertex.
     *
     * @return The radius of the vertex
     */
    public static int getVertexRadius() {
        return VERTEX_RADIUS;
    }

    /**
     * Returns the default color of the vertex.
     *
     * @return The default color of the vertex
     */
    public static Color getVertexColor() {
        return VERTEX_COLOR;
    }

    /**
     * Returns the selected color of the vertex.
     *
     * @return The selected color of the vertex
     */
    public static Color getVertexSelectedColor() {
        return VERTEX_SELECTED_COLOR;
    }

    /**
     * Returns the label of the vertex.
     *
     * @return The label of the vertex
     */
    public String getLabel() {
        return vertexLabel;
    }
}
