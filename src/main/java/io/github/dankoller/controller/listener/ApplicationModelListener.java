package io.github.dankoller.controller.listener;

import io.github.dankoller.controller.algorithm.Algorithm;
import io.github.dankoller.model.AlgorithmModel;
import io.github.dankoller.model.Mode;

/**
 * Interface for listeners of the application model.
 */
public interface ApplicationModelListener {
    default void onModelChangeSetMode(Mode mode) {
    }

    default void onModelChangeSetAlgorithm(Algorithm algorithm) {
    }

    default void onModelChangeSetAlgorithmState(AlgorithmModel algorithmModel) {
    }
}
