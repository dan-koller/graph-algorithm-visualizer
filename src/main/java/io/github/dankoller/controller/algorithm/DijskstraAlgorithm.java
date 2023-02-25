package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;
import io.github.dankoller.model.VertexModel;
import io.github.dankoller.model.VertexAndRouteModel;
import io.github.dankoller.view.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class DijskstraAlgorithm extends Algorithm {
    private final PriorityQueue<VertexAndRouteModel> vertexQueue =
            new PriorityQueue<>(Comparator.comparingInt(VertexAndRouteModel::routeLength));
    private final Set<VertexModel> visitedNodes = new HashSet<>();
    private final Map<VertexModel, Integer> routeLengths = new HashMap<>();

    /**
     * Resets the collections (vertices, visited nodes, route lengths) to their initial state.
     */
    private void resetCollections() {
        vertexQueue.clear();
        visitedNodes.clear();
        routeLengths.clear();
    }

    /**
     * Performs the Dijkstra algorithm on the given vertex.
     *
     * @param vertex The vertex to start the algorithm from
     */
    @Override
    public void performAlgorithm(Vertex vertex) {
        resetCollections();
        setAlgorithmResult(dijkstraRoute(vertex));
        getPlayer().play(new ArrayDeque<>());
    }

    /**
     * Calculates the route for the Dijkstra algorithm.
     *
     * @param vertex The vertex to start the algorithm from
     * @return The route as a string
     */
    private String dijkstraRoute(Vertex vertex) {
        var startVertex = getApplicationModel().getModel().getModelVertex(vertex);
        vertexQueue.offer(new VertexAndRouteModel(startVertex, 0));
        routeLengths.put(startVertex, 0);
        while (!vertexQueue.isEmpty()) {
            VertexModel currentVertex = vertexQueue.poll().vertex();
            findNextUnvisitedNode(currentVertex);
            visitedNodes.add(currentVertex);
        }
        return routeLengths.entrySet().stream().filter(entry -> entry.getValue() > 0)
                .map(entry -> "%s=%d".formatted(entry.getKey().getVertex().getLabel(), entry.getValue()))
                .sorted()
                .collect(Collectors.joining(", "));
    }

    /**
     * Searches for the next unvisited node and updates the route lengths.
     *
     * @param vertex The vertex to search from
     */
    private void findNextUnvisitedNode(VertexModel vertex) {
        vertex.getEdges().stream()
                .filter(edge -> !visitedNodes.contains(edge.end()))
                .forEach(edge -> dijkstraUpdateNeighbor(vertex, edge, Integer.parseInt(edge.weightLabel().getText())));
    }

    /**
     * Updates the route length for the given neighbor according to the Dijkstra algorithm.
     *
     * @param vertex         The vertex to search from
     * @param edgeToNeighbor The edge to the neighbor
     * @param edgeWeight     The weight of the edge
     */
    private void dijkstraUpdateNeighbor(VertexModel vertex, EdgeModel edgeToNeighbor, int edgeWeight) {
        int lengthOnThisRoute = routeLengths.get(vertex) + edgeWeight;
        VertexModel neighbor = edgeToNeighbor.end();
        int lengthSoFar = Optional.ofNullable(routeLengths.get(neighbor)).orElse(Integer.MAX_VALUE);
        if (lengthOnThisRoute < lengthSoFar) {
            vertexQueue.offer(new VertexAndRouteModel(neighbor, lengthOnThisRoute));
            routeLengths.put(neighbor, lengthOnThisRoute);
        }
    }
}
