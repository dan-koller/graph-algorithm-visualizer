package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;
import io.github.dankoller.model.VertexModel;
import io.github.dankoller.view.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class PrimAlgorithm extends Algorithm {
    private final Set<EdgeModel> currentEdges = new HashSet<>();
    private final Set<VertexModel> connected = new HashSet<>();
    private final Queue<EdgeModel> minimumSpanningTree = new ArrayDeque<>();

    /**
     * Resets the collections (currentEdges, connected, spanningTree) to their initial state.
     */
    private void resetCollections() {
        currentEdges.clear();
        connected.clear();
        minimumSpanningTree.clear();
    }

    /**
     * Performs the Prim algorithm on the given vertex.
     *
     * @param vertex The vertex to start the algorithm from
     */
    @Override
    public void performAlgorithm(Vertex vertex) {
        resetCollections();
        setAlgorithmResult(primRoute(vertex));
        getPlayer().play(minimumSpanningTree);
    }

    /**
     * Calculates the route for the Prim algorithm.
     *
     * @param vertex The vertex to start the algorithm from
     * @return The route as a string
     */
    private String primRoute(Vertex vertex) {
        getApplicationModel().getModel().selectVertex(vertex);
        var startVertex = getApplicationModel().getModel().getModelVertex(vertex);
        connect(startVertex);
        while (!currentEdges.isEmpty()) {
            var nextEdgeToConnect = findMinimalWeightEdge();
            minimumSpanningTree.offer(nextEdgeToConnect);
            connect(nextEdgeToConnect.end());
        }
        return minimumSpanningTree.stream()
                .map(edge -> "%s=%s".formatted(edge.end().getVertex().getLabel(),
                        edge.start().getVertex().getLabel()))
                .sorted().collect(Collectors.joining(", "));
    }

    /**
     * Helper method to find the edge with the minimal weight.
     *
     * @return The edge with the minimal weight
     */
    private EdgeModel findMinimalWeightEdge() {
        return currentEdges.stream()
                .min(Comparator.comparingInt(edge -> Integer.parseInt(edge.weightLabel().getText())))
                .orElseThrow(() -> new IllegalStateException("No edge found"));
    }

    /**
     * Connects the given vertex to the minimum spanning tree.
     *
     * @param vertex The vertex to connect
     */
    private void connect(VertexModel vertex) {
        connected.add(vertex);
        currentEdges.stream().filter(edge -> edge.end() == vertex).toList().forEach(currentEdges::remove);
        currentEdges.addAll(vertex.getEdges().stream()
                .filter(edge -> !connected.contains(edge.end())).toList());
    }
}
