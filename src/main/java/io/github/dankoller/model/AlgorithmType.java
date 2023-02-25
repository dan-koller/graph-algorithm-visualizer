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

    /**
     * Create a new algorithm type.
     *
     * @param algorithmName     Name of the algorithm
     * @param algorithmSupplier Supplier of the algorithm
     */
    AlgorithmType(String algorithmName, Supplier<Algorithm> algorithmSupplier) {
        this.algorithmName = algorithmName;
        this.algorithmSupplier = algorithmSupplier;
    }

    /**
     * Get the name of the algorithm.
     *
     * @return Name of the algorithm
     */
    public String getAlgorithmName() {
        return algorithmName;
    }

    /**
     * Get the supplier of the algorithm. The supplier is used to create a new instance of the algorithm.
     *
     * @return Supplier of the algorithm
     */
    public Supplier<Algorithm> getAlgorithmSupplier() {
        return algorithmSupplier;
    }
}
