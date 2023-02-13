package io.github.dankoller.controller;

import io.github.dankoller.view.ApplicationPanel;
import io.github.dankoller.view.Edge;
import io.github.dankoller.view.Vertex;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static io.github.dankoller.view.Vertex.getVertexRadius;

public class GraphClickListener extends MouseAdapter {
    private static final int CLICK_OFFSET = getVertexRadius() / 2;
    private final ApplicationPanel panel;

    public GraphClickListener(ApplicationPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (panel.getMode()) {
            case ADD_VERTEX -> placeVertexFromEvent(e);
            case ADD_EDGE -> placeEdgeFromEvent(e);
            case REMOVE_VERTEX -> removeVertexFromEvent(e);
            case REMOVE_EDGE -> removeEdgeFromEvent(e);
            case NONE -> {/* Do nothing */}
            default -> throw new IllegalStateException("Unexpected value: " + panel.getMode());
        }
    }

    private void removeEdgeFromEvent(MouseEvent event) {
        if (panel.getComponentAt(event.getPoint()) instanceof Edge edge) {
            panel.removeEdge(edge);
        }
    }

    private void removeVertexFromEvent(MouseEvent event) {
        panel.getVertexAt(event.getPoint()).ifPresent(panel::removeVertexWithAssociateEdges);
    }

    private void placeEdgeFromEvent(MouseEvent event) {
        panel.getVertexAt(event.getPoint()).ifPresent(vertex -> panel.getSelectedVertex()
                .ifPresentOrElse(selected -> addEdge(selected, vertex), () -> panel.setSelectedVertex(vertex)));
    }

    private void addEdge(Vertex from, Vertex to) {
        if (from == to) {
            return;
        }
        new Dialog().getLabelFromDialog(panel).ifPresent(weight -> panel.addEdge(weight, from, to));
        from.deselect();
        panel.setSelectedVertex(null);
    }

    private void placeVertexFromEvent(MouseEvent event) {
        Point point = new Point(event.getX() - CLICK_OFFSET, event.getY() - CLICK_OFFSET);
        panel.getVertexAt(event.getPoint()).ifPresentOrElse(Vertex::select, () ->
                new Dialog().getLabelFromDialog(panel).ifPresent(label -> panel.addVertex(label, point)));
    }
}
