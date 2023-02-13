package io.github.dankoller.model;

import io.github.dankoller.controller.algorithm.Algorithm;
import io.github.dankoller.controller.listener.ApplicationModelListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static io.github.dankoller.model.State.STOPPED;

public class ApplicationModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final GraphModel model = new GraphModel();
    private transient AlgorithmModel algorithmModel = null;
    private final transient List<ApplicationModelListener> listeners = new ArrayList<>();

    public void registerListener(ApplicationModelListener listener) {
        listeners.add(listener);
    }

    private void notifyModeUpdate(Mode mode) {
        listeners.forEach(listener -> listener.onModelChangeSetMode(mode));
    }

    private void notifyAlgorithmUpdate(Algorithm algorithm) {
        listeners.forEach(listener -> listener.onModelChangeSetAlgorithm(algorithm));
    }

    private void notifyAlgorithmStateUpdate(AlgorithmModel algorithmModel) {
        listeners.forEach(listener -> listener.onModelChangeSetAlgorithmState(algorithmModel));
    }

    public void setMode(Mode mode) {
        if (algorithmIsRunning()) {
            switchAlgorithmState(STOPPED);
        }
        notifyModeUpdate(mode);
    }

    private boolean algorithmIsRunning() {
        return algorithmModel != null && algorithmModel.getState() != STOPPED;
    }

    public void switchAlgorithmState(State newState) {
        algorithmModel.setState(newState);
        notifyAlgorithmStateUpdate(algorithmModel);
    }

    // Callback used in propagation of algorithm result
    public void propagateAlgorithmResult(String result) {
        algorithmModel.setResultText(result);
        switchAlgorithmState(State.TERMINATED);
    }

    public void startAlgorithm(AlgorithmType algorithmType) {
        setMode(Mode.NONE);
        notifyAlgorithmUpdate(algorithmType.getAlgorithmSupplier().get().setApplicationModel(this));
        algorithmModel = new AlgorithmModel();
        notifyAlgorithmStateUpdate(algorithmModel);
    }

    public void requestResetGraph() {
        setMode(Mode.RESET_MODE);
    }

    public GraphModel getModel() {
        return model;
    }
}
