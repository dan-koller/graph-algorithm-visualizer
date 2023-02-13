package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;
import io.github.dankoller.model.VertexModel;
import io.github.dankoller.view.Vertex;

import java.util.Queue;
import java.util.stream.Collectors;

public class BreadthFirstSearch extends Algorithm {
    @Override
    public void performAlgorithm(Vertex vertex) {
        Queue<EdgeModel> traversal = getApplicationModel().getModel().breadthFirstSearch(vertex);
        String result = String.format("BFS : %s", vertex.getLabel());
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
