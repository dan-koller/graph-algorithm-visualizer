package io.github.dankoller.controller.listener;

import io.github.dankoller.controller.Dialog;
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

    /**
     * When creating a new GraphClickListener, the ApplicationPanel is passed in to allow the listener to access
     * the current mode and to add/remove vertices and edges.
     *
     * @param panel The ApplicationPanel
     */
    public GraphClickListener(ApplicationPanel panel) {
        this.panel = panel;
    }

    /**
     * The mouseClicked method is called when the mouse is clicked. Depending on the current mode,
     * the method will either add a vertex, add an edge, remove a vertex or remove an edge.
     *
     * @param e The event to be processed
     */
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

    /**
     * Handle the removal of an edge from the panel if the user has selected the remove edge mode and clicked on an
     * edge.
     *
     * @param event The event to be processed
     */
    private void removeEdgeFromEvent(MouseEvent event) {
        if (panel.getComponentAt(event.getPoint()) instanceof Edge edge) {
            panel.removeEdge(edge);
        }
    }

    /**
     * Handle the removal of a vertex from the panel if the user has selected the remove vertex mode and clicked on a
     * vertex.
     *
     * @param event The event to be processed
     */
    private void removeVertexFromEvent(MouseEvent event) {
        panel.getVertexAt(event.getPoint()).ifPresent(panel::removeVertexWithAssociateEdges);
    }

    /**
     * Handle the placement of an edge from the panel if the user has selected the add edge mode and clicked on a
     * vertex.
     *
     * @param event The event to be processed
     */
    private void placeEdgeFromEvent(MouseEvent event) {
        panel.getVertexAt(event.getPoint()).ifPresent(vertex -> panel.getSelectedVertex()
                .ifPresentOrElse(selected -> addEdge(selected, vertex), () -> panel.setSelectedVertex(vertex)));
    }

    /**
     * Add an edge between two vertices.
     *
     * @param from The vertex to add the edge from
     * @param to   The vertex to add the edge to
     */
    private void addEdge(Vertex from, Vertex to) {
        if (from == to) {
            return;
        }
        new Dialog().getLabelFromDialog(panel).ifPresent(weight -> panel.addEdge(weight, from, to));
        from.deselect();
        panel.setSelectedVertex(null);
    }

    /**
     * Place a vertex on the point where the user clicked if the user has selected the add vertex mode.
     *
     * @param event The event to be processed
     */
    private void placeVertexFromEvent(MouseEvent event) {
        Point point = new Point(event.getX() - CLICK_OFFSET, event.getY() - CLICK_OFFSET);
        panel.getVertexAt(event.getPoint()).ifPresentOrElse(Vertex::select, () ->
                new Dialog().getLabelFromDialog(panel).ifPresent(label -> panel.addVertex(label, point)));
    }
}
