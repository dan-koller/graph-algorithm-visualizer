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

    /**
     * Add listeners from the main frame to the model.
     *
     * @param listener Listener to add
     */
    public void registerListener(ApplicationModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notify all listeners that the mode has changed.
     *
     * @param mode New mode
     */
    private void notifyModeUpdate(Mode mode) {
        listeners.forEach(listener -> listener.onModelChangeSetMode(mode));
    }

    /**
     * Notify all listeners that the algorithm has changed.
     *
     * @param algorithm New algorithm
     */
    private void notifyAlgorithmUpdate(Algorithm algorithm) {
        listeners.forEach(listener -> listener.onModelChangeSetAlgorithm(algorithm));
    }

    /**
     * Notify all listeners that the algorithm state has changed.
     *
     * @param algorithmModel New algorithm model
     */
    private void notifyAlgorithmStateUpdate(AlgorithmModel algorithmModel) {
        listeners.forEach(listener -> listener.onModelChangeSetAlgorithmState(algorithmModel));
    }

    /**
     * Set the mode of the application.
     *
     * @param mode New mode
     */
    public void setMode(Mode mode) {
        if (algorithmIsRunning()) {
            switchAlgorithmState(STOPPED);
        }
        notifyModeUpdate(mode);
    }

    /**
     * Check if an algorithm is currently running.
     *
     * @return True if an algorithm is running, false otherwise
     */
    private boolean algorithmIsRunning() {
        return algorithmModel != null && algorithmModel.getState() != STOPPED;
    }

    /**
     * Switch the state of the algorithm.
     *
     * @param newState New state
     */
    public void switchAlgorithmState(State newState) {
        algorithmModel.setState(newState);
        notifyAlgorithmStateUpdate(algorithmModel);
    }

    /**
     * Callback method to propagate the result of the algorithm.
     *
     * @param result Result of the algorithm
     */
    public void propagateAlgorithmResult(String result) {
        algorithmModel.setResultText(result);
        switchAlgorithmState(State.TERMINATED);
    }

    /**
     * Start an algorithm. The mode is reset to not interfere with the running algorithm.
     *
     * @param algorithmType Type of the algorithm
     */
    public void startAlgorithm(AlgorithmType algorithmType) {
        setMode(Mode.NONE);
        notifyAlgorithmUpdate(algorithmType.getAlgorithmSupplier().get().setApplicationModel(this));
        algorithmModel = new AlgorithmModel();
        notifyAlgorithmStateUpdate(algorithmModel);
    }

    /**
     * Request a reset of the graph.
     */
    public void requestResetGraph() {
        setMode(Mode.RESET_MODE);
    }

    /**
     * Get the graph model.
     *
     * @return Graph model
     */
    public GraphModel getModel() {
        return model;
    }
}
