package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;
import io.github.dankoller.model.VertexModel;
import io.github.dankoller.view.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class PrimAlgorithm extends Algorithm {
    private final Set<EdgeModel> currentEdges = new HashSet<>();
    private final Set<VertexModel> connected = new HashSet<>();
    private final Queue<EdgeModel> spanningTree = new ArrayDeque<>();

    private void resetCollections() {
        currentEdges.clear();
        connected.clear();
        spanningTree.clear();
    }

    @Override
    public void performAlgorithm(Vertex vertex) {
        resetCollections();
        setAlgorithmResult(primRoute(vertex));
        getPlayer().play(spanningTree);
    }

    private String primRoute(Vertex vertex) {
        getApplicationModel().getModel().selectVertex(vertex);
        var startVertex = getApplicationModel().getModel().getModelVertex(vertex);
        connect(startVertex);
        while (!currentEdges.isEmpty()) {
            var nextEdgeToConnect = findMinimalWeightEdge();
            spanningTree.offer(nextEdgeToConnect);
            connect(nextEdgeToConnect.end());
        }
        return spanningTree.stream()
                .map(edge -> "%s=%s".formatted(edge.end().getVertex().getLabel(),
                        edge.start().getVertex().getLabel()))
                .sorted().collect(Collectors.joining(", "));
    }

    private EdgeModel findMinimalWeightEdge() {
        return currentEdges.stream()
                .min(Comparator.comparingInt(edge -> Integer.parseInt(edge.weightLabel().getText())))
                .orElseThrow(() -> new IllegalStateException("No edge found"));
    }

    private void connect(VertexModel vertex) {
        connected.add(vertex);
        currentEdges.stream().filter(edge -> edge.end() == vertex).toList().forEach(currentEdges::remove);
        currentEdges.addAll(vertex.getEdges().stream()
                .filter(edge -> !connected.contains(edge.end())).toList());
    }
}
