package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;
import io.github.dankoller.model.VertexModel;
import io.github.dankoller.view.Vertex;

import java.util.Queue;
import java.util.stream.Collectors;

public class DepthFirstSearch extends Algorithm {
    /**
     * Performs the Depth First Search algorithm on the given vertex.
     *
     * @param vertex The vertex to start the algorithm from
     */
    @Override
    public void performAlgorithm(Vertex vertex) {
        Queue<EdgeModel> traversal = getApplicationModel().getModel().depthFirstSearch(vertex);
        String result = String.format("DFS : %s", vertex.getLabel());
        if (!traversal.isEmpty()) {
            result += " -> " + traversal.stream()
                    .map(EdgeModel::end)
                    .map(VertexModel::getVertex)
                    .map(Vertex::getLabel)
                    .collect(Collectors.joining(" -> "));
        }
        setAlgorithmResult(result);
        getPlayer().play(traversal);
    }
}
