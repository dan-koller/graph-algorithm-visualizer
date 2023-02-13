package io.github.dankoller.model;

import io.github.dankoller.view.Vertex;

import java.util.ArrayList;
import java.util.List;

public class VertexModel {
    private final Vertex vertex;
    private boolean visited = false;
    private final List<EdgeModel> edges = new ArrayList<>();

    public VertexModel(Vertex vertex) {
        this.vertex = vertex;
    }

    public void addTreeEdge(EdgeModel edge) {
        edges.add(edge);
    }

    // Getters and setters
    public Vertex getVertex() {
        return vertex;
    }

    public List<EdgeModel> getEdges() {
        return edges;
    }

    public boolean isVisited() {
        return !visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
