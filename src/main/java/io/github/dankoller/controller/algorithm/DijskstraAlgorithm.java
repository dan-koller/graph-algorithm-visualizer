package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;
import io.github.dankoller.model.VertexModel;
import io.github.dankoller.model.VertexAndRouteModel;
import io.github.dankoller.view.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class DijskstraAlgorithm extends Algorithm {
    private final PriorityQueue<VertexAndRouteModel> vertexQueue
            = new PriorityQueue<>(Comparator.comparingInt(VertexAndRouteModel::routeLength));
    private final Set<VertexModel> visitedNodes = new HashSet<>();
    private final Map<VertexModel, Integer> routeLengths = new HashMap<>();

    private void resetCollections() {
        vertexQueue.clear();
        visitedNodes.clear();
        routeLengths.clear();
    }

    @Override
    public void performAlgorithm(Vertex vertex) {
        resetCollections();
        setAlgorithmResult(dijkstraRoute(vertex));
        getPlayer().play(new ArrayDeque<>());
    }

    private String dijkstraRoute(Vertex vertex) {
        var startVertex = getApplicationModel().getModel().getModelVertex(vertex);
        vertexQueue.offer(new VertexAndRouteModel(startVertex, 0));
        routeLengths.put(startVertex, 0);
        while (!vertexQueue.isEmpty()) {
            var currentVertex = vertexQueue.poll().vertex();
            findNextUnvisitedNode(currentVertex);
            visitedNodes.add(currentVertex);
        }
        return routeLengths.entrySet().stream().filter(entry -> entry.getValue() > 0)
                .map(entry -> "%s=%d".formatted(entry.getKey().getVertex().getLabel(), entry.getValue()))
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private void findNextUnvisitedNode(VertexModel vertex) {
        vertex.getEdges().stream()
                .filter(edge -> !visitedNodes.contains(edge.end()))
                .forEach(edge -> dijkstraUpdateNeighbor(vertex, edge, Integer.parseInt(edge.weightLabel().getText())));
    }

    private void dijkstraUpdateNeighbor(VertexModel vertex, EdgeModel edgeToNeighbor, int edgeWeight) {
        int lengthOnThisRoute = routeLengths.get(vertex) + edgeWeight;
        var neighbor = edgeToNeighbor.end();
        int lengthSoFar = Optional.ofNullable(routeLengths.get(neighbor)).orElse(Integer.MAX_VALUE);
        if (lengthOnThisRoute < lengthSoFar) {
            vertexQueue.offer(new VertexAndRouteModel(neighbor, lengthOnThisRoute));
            routeLengths.put(neighbor, lengthOnThisRoute);
        }
    }
}
