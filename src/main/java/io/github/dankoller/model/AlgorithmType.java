package io.github.dankoller.model;

import io.github.dankoller.controller.algorithm.*;

import java.util.function.Supplier;

public enum AlgorithmType {
    DEPTH_FIRST("Depth-First Search", DepthFirstSearch::new),
    BREADTH_FIRST("Breadth-First Search", BreadthFirstSearch::new),
    DIJKSTRA_ALGORITHM("Dijkstra's Algorithm", DijskstraAlgorithm::new),
    PRIM_ALGORITHM("Prim's Algorithm", PrimAlgorithm::new);

    private final String algorithmName;
    private final Supplier<Algorithm> algorithmSupplier;

    AlgorithmType(String algorithmName, Supplier<Algorithm> algorithmSupplier) {
        this.algorithmName = algorithmName;
        this.algorithmSupplier = algorithmSupplier;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public Supplier<Algorithm> getAlgorithmSupplier() {
        return algorithmSupplier;
    }
}
