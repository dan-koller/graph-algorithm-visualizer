package io.github.dankoller.model;

import io.github.dankoller.view.Edge;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Set;

public record EdgeModel(Edge from, Edge to, JLabel weightLabel, VertexModel start, VertexModel end) {
    public Collection<Component> getEdgeComponents() {
        return Set.of(from, to, weightLabel);
    }
}