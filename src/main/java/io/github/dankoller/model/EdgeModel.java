package io.github.dankoller.model;

import io.github.dankoller.view.Edge;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public record EdgeModel(Edge from, Edge to, JLabel weightLabel, VertexModel start, VertexModel end) {
    /**
     * Get the components of the edge. This includes the start and end vertices, and the weight label.
     *
     * @return The components of the edge
     */
    public Set<Component> getEdgeComponents() {
        return Set.of(from, to, weightLabel);
    }
}
