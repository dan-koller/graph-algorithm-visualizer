package io.github.dankoller.controller.listener;

import io.github.dankoller.controller.algorithm.Algorithm;
import io.github.dankoller.model.AlgorithmModel;
import io.github.dankoller.model.Mode;

public interface ApplicationModelListener {
    default void onModelChangeSetMode(Mode mode) {}
    default void onModelChangeSetAlgorithm(Algorithm algorithm) {}
    default void onModelChangeSetAlgorithmState(AlgorithmModel algorithmModel) {}
}
