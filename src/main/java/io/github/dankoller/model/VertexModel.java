package io.github.dankoller.model;

import io.github.dankoller.view.Vertex;

import java.util.ArrayList;
import java.util.List;

public class VertexModel {
    private final Vertex vertex;
    private boolean visited = false;
    private final List<EdgeModel> edges = new ArrayList<>();

    /**
     * Creates a new VertexModel with the given vertex.
     *
     * @param vertex The vertex to create the model for.
     */
    public VertexModel(Vertex vertex) {
        this.vertex = vertex;
    }

    /**
     * Adds an edge to the vertex.
     *
     * @param edge The edge to add.
     */
    public void addTreeEdge(EdgeModel edge) {
        edges.add(edge);
    }

    /**
     * Returns the vertex.
     *
     * @return The vertex.
     */
    public Vertex getVertex() {
        return vertex;
    }

    /**
     * Get all edges of the vertex.
     *
     * @return A list of all edges.
     */
    public List<EdgeModel> getEdges() {
        return edges;
    }

    /**
     * Returns whether the vertex has been visited. This is necessary for the DFS and BFS algorithms.
     *
     * @return True if the vertex has been visited, false otherwise.
     */
    public boolean isVisited() {
        return !visited;
    }

    /**
     * Sets the visited flag of the vertex. This is necessary for the DFS and BFS algorithms.
     *
     * @param visited The new value of the visited flag.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
