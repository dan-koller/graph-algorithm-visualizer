package io.github.dankoller.model;

import io.github.dankoller.view.Edge;
import io.github.dankoller.view.Vertex;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class GraphModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 12L;
    private final Map<Point, VertexModel> vertices = new HashMap<>();
    private final List<Edge> edges = new ArrayList<>();

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex The vertex to add
     */
    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getCenter(), new VertexModel(vertex));
    }

    /**
     * Adds an edge to the graph.
     *
     * @param edge         The edge to add
     * @param oppositeEdge The opposite edge to add
     * @param weightLabel  The label for the weight of the edge
     */
    public void addEdge(Edge edge, Edge oppositeEdge, JLabel weightLabel) {
        edges.add(edge);
        VertexModel start = vertices.get(edge.getStart());
        VertexModel end = vertices.get(edge.getEnd());
        start.addTreeEdge(new EdgeModel(edge, oppositeEdge, weightLabel, start, end));
        end.addTreeEdge(new EdgeModel(oppositeEdge, edge, weightLabel, end, start));
    }

    /**
     * Returns the vertex model for the given vertex.
     *
     * @param vertex The vertex to get the model for
     * @return The vertex model
     */
    public VertexModel getModelVertex(Vertex vertex) {
        return vertices.get(vertex.getCenter());
    }

    /**
     * Handles the removal of a vertex and its edges. This method is called when a vertex is removed from the graph.
     * It returns a set of components to remove from the graph. The application panel will then remove these components
     * and repaint the graph.
     *
     * @param vertex The vertex to remove
     * @return A set of components to remove
     */
    public Set<Component> removeVertexWithEdges(Vertex vertex) {
        VertexModel vertexModel = vertices.get(vertex.getCenter());
        Set<Component> componentsToRemove = new HashSet<>();
        componentsToRemove.add(vertex);
        vertexModel.getEdges().forEach(treeEdge -> removeEdgeFromModelAndUpdateComponents(componentsToRemove, treeEdge));
        vertices.remove(vertex.getCenter());
        return componentsToRemove;
    }

    /**
     * Handles the removal of an edge. This method is called when an edge is removed from the graph.
     *
     * @param componentsToRemove A set of components to remove
     * @param edgeModel          The edge model to remove
     */
    private void removeEdgeFromModelAndUpdateComponents(Set<Component> componentsToRemove, EdgeModel edgeModel) {
        componentsToRemove.addAll(edgeModel.getEdgeComponents());
        edges.remove(edgeModel.to());
        edges.remove(edgeModel.from());
        Optional<EdgeModel> oppositeTreeEdge = edgeModel.end().getEdges().stream()
                .filter(te -> te.to() == edgeModel.from()).findFirst();
        oppositeTreeEdge.ifPresent(edgeModel.end().getEdges()::remove);
    }

    /**
     * Handles the removal of an edge. This method is called when an edge is removed from the graph.
     * It returns a set of components to remove from the graph. The application panel will then remove these components
     * and repaint the graph.
     *
     * @param edge The edge to remove
     * @return A set of components to remove
     */
    public Set<Component> removeEdge(Edge edge) {
        VertexModel start = vertices.get(edge.getStart());
        Set<Component> edgeComponentsToRemove = new HashSet<>();
        Optional<EdgeModel> optional = start.getEdges().stream().filter(te -> te.from() == edge).findFirst();
        optional.ifPresent(treeEdge -> {
            removeEdgeFromModelAndUpdateComponents(edgeComponentsToRemove, treeEdge);
            start.getEdges().remove(treeEdge);
        });
        return edgeComponentsToRemove;
    }

    /**
     * Clears the graph if the user wants to start over.
     */
    public void clear() {
        vertices.clear();
        edges.clear();
    }

    /**
     * Unselects all vertices and edges.
     */
    public void unselect() {
        vertices.values().forEach(vertexModel -> {
            vertexModel.setVisited(false);
            vertexModel.getVertex().deselect();
        });
        edges.forEach(Edge::unselect);
    }

    /**
     * Selects the given vertex.
     *
     * @param vertex The vertex to select
     */
    public void selectVertex(Vertex vertex) {
        vertices.get(vertex.getCenter());
        vertex.select();
    }

    /**
     * Selects the given edge vertex from a vertex model.
     *
     * @param vertex The vertex model to select
     */
    private void selectVertex(VertexModel vertex) {
        vertex.setVisited(true);
        vertex.getVertex().select();
    }

    /**
     * Selects the given edge and its neighbor vertex.
     *
     * @param edge The edge to select
     */
    public void selectEdgeAndNeighborVertex(EdgeModel edge) {
        selectVertex(edge.end());
        if (edges.contains(edge.from())) {
            edge.from().select();
        } else {
            edge.to().select();
        }
    }

    /**
     * This method initializes the graph for a breadth first search.
     *
     * @param startVertex The vertex to start the search from
     * @return A queue of edges that were traversed
     */
    public Queue<EdgeModel> depthFirstSearch(Vertex startVertex) {
        Queue<EdgeModel> traverseQueue = new ArrayDeque<>();
        VertexModel start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return depthFirstSearch(traverseQueue, start);
    }

    /**
     * This method performs a depth first search on the graph.
     *
     * @param traverseQueue The queue of edges that were traversed
     * @param vertex        The vertex to start the search from
     * @return A queue of edges that were traversed
     */
    private Queue<EdgeModel> depthFirstSearch(Queue<EdgeModel> traverseQueue, VertexModel vertex) {
        for (EdgeModel edge : vertex.getEdges()) {
            if (edge.end().isVisited()) {
                edge.end().setVisited(true);
                traverseQueue.offer(edge);
                depthFirstSearch(traverseQueue, edge.end());
            }
        }
        return traverseQueue;
    }

    /**
     * This method initializes the graph for a breadth first search.
     *
     * @param startVertex The vertex to start the search from
     * @return A queue of edges that were traversed
     */
    public Queue<EdgeModel> breadthFirstSearch(Vertex startVertex) {
        Queue<EdgeModel> traverseQueue = new ArrayDeque<>();
        VertexModel start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return breadthFirstSearch(traverseQueue, List.of(vertices.get(startVertex.getCenter())));
    }

    /**
     * This method performs a breadth first search on the graph.
     *
     * @param traverseQueue The queue of edges that were traversed
     * @param levelVertices The vertices to start the search from
     * @return A queue of edges that were traversed
     */
    private Queue<EdgeModel> breadthFirstSearch(Queue<EdgeModel> traverseQueue, List<VertexModel> levelVertices) {
        if (levelVertices.isEmpty()) {
            return traverseQueue;
        }
        List<EdgeModel> unvisitedNeighborEdges = levelVertices.stream()
                .flatMap(vertex -> vertex.getEdges().stream())
                .filter(edge -> edge.end().isVisited()).toList();
        unvisitedNeighborEdges.forEach(edge -> {
            if (edge.end().isVisited()) {
                edge.end().setVisited(true);
                traverseQueue.offer(edge);
            }
        });
        breadthFirstSearch(traverseQueue, unvisitedNeighborEdges.stream().map(EdgeModel::end).toList());
        return traverseQueue;
    }

    /**
     * Get a list of all edges in the graph.
     *
     * @return A list of all edges in the graph
     */
    public List<Edge> getEdges() {
        return edges;
    }
}
