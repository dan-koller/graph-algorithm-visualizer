package io.github.dankoller.view;

import javax.swing.*;
import java.awt.*;

public class Edge extends JComponent {
    private final Point start;
    private final Point end;
    private boolean selected;

    public Edge(Vertex from, Vertex to) {
        super();
        setName("Edge <%s -> %s>".formatted(from.getLabel(), to.getLabel()));
        this.start = from.getCenter();
        this.end = to.getCenter();
    }

    public void select() {
        if (!selected) {
            selected = true;
            getParent().repaint();
        }
    }

    public void unselect() {
        if (selected) {
            selected = false;
            getParent().repaint();
        }
    }

    // Getters
    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public boolean isSelected() {
        return selected;
    }
}
