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

    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getCenter(), new VertexModel(vertex));
    }

    public void addEdge(Edge edge, Edge oppositeEdge, JLabel weightLabel) {
        edges.add(edge);
        VertexModel start = vertices.get(edge.getStart());
        VertexModel end = vertices.get(edge.getEnd());
        start.addTreeEdge(new EdgeModel(edge, oppositeEdge, weightLabel, start, end));
        end.addTreeEdge(new EdgeModel(oppositeEdge, edge, weightLabel, end, start));
    }

    public VertexModel getModelVertex(Vertex vertex) {
        return vertices.get(vertex.getCenter());
    }

    public Set<Component> removeVertexWithEdges(Vertex vertex) {
        VertexModel vertexModel = vertices.get(vertex.getCenter());
        Set<Component> componentsToRemove = new HashSet<>();
        componentsToRemove.add(vertex);
        vertexModel.getEdges().forEach(treeEdge -> removeEdgeFromModelAndAddComponents(componentsToRemove, treeEdge));
        vertices.remove(vertex.getCenter());
        return componentsToRemove;
    }

    private void removeEdgeFromModelAndAddComponents(Set<Component> componentsToRemove, EdgeModel edgeModel) {
        componentsToRemove.addAll(edgeModel.getEdgeComponents());
        edges.remove(edgeModel.to());
        edges.remove(edgeModel.from());
        Optional<EdgeModel> oppositeTreeEdge = edgeModel.end().getEdges().stream()
                .filter(te -> te.to() == edgeModel.from()).findFirst();
        oppositeTreeEdge.ifPresent(edgeModel.end().getEdges()::remove);
    }

    public Set<Component> removeEdge(Edge edge) {
        VertexModel start = vertices.get(edge.getStart());
        Set<Component> edgeComponentsToRemove = new HashSet<>();
        Optional<EdgeModel> optional = start.getEdges().stream().filter(te -> te.from() == edge).findFirst();
        optional.ifPresent(treeEdge -> {
            removeEdgeFromModelAndAddComponents(edgeComponentsToRemove, treeEdge);
            start.getEdges().remove(treeEdge);
        });
        return edgeComponentsToRemove;
    }

    public void clear() {
        vertices.clear();
        edges.clear();
    }

    public void unselect() {
        vertices.values().forEach(vertexModel -> {
            vertexModel.setVisited(false);
            vertexModel.getVertex().deselect();
        });
        edges.forEach(Edge::unselect);
    }

    public void selectVertex(Vertex vertex) {
        vertices.get(vertex.getCenter());
        vertex.select();
    }

    private void selectVertex(VertexModel vertex) {
        vertex.setVisited(true);
        vertex.getVertex().select();
    }

    public void selectEdgeAndNeighborVertex(EdgeModel edge) {
        selectVertex(edge.end());
        if (edges.contains(edge.from())) {
            edge.from().select();
        } else {
            edge.to().select();
        }
    }

    public Queue<EdgeModel> depthFirstSearch(Vertex startVertex) {
        Queue<EdgeModel> traverseQueue = new ArrayDeque<>();
        VertexModel start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return depthFirstSearch(traverseQueue, start);
    }

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

    public Queue<EdgeModel> breadthFirstSearch(Vertex startVertex) {
        Queue<EdgeModel> traverseQueue = new ArrayDeque<>();
        VertexModel start = vertices.get(startVertex.getCenter());
        selectVertex(start);
        return breadthFirstSearch(traverseQueue, List.of(vertices.get(startVertex.getCenter())));
    }

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

    public List<Edge> getEdges() {
        return edges;
    }
}
