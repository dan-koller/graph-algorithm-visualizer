package io.github.dankoller.view;

import javax.swing.*;
import java.awt.*;

public class Edge extends JComponent {
    private final Point start;
    private final Point end;
    private boolean selected;

    /**
     * Creates an edge between two vertices (from -> to).
     *
     * @param from The vertex the edge starts from
     * @param to   The vertex the edge ends at
     */
    public Edge(Vertex from, Vertex to) {
        super();
        setName("Edge <%s -> %s>".formatted(from.getLabel(), to.getLabel()));
        this.start = from.getCenter();
        this.end = to.getCenter();
    }

    /**
     * Selects the edge and repaints the parent component if necessary.
     */
    public void select() {
        if (!selected) {
            selected = true;
            getParent().repaint();
        }
    }

    /**
     * Unselects the edge and repaints the parent component if necessary.
     */
    public void unselect() {
        if (selected) {
            selected = false;
            getParent().repaint();
        }
    }

    /**
     * Returns the start and end points of the edge.
     */
    public Point getStart() {
        return start;
    }

    /**
     * Returns the start and end points of the edge.
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Returns whether the edge is selected.
     */
    public boolean isSelected() {
        return selected;
    }
}
