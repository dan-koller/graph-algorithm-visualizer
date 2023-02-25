package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.ApplicationModel;
import io.github.dankoller.model.State;
import io.github.dankoller.view.ApplicationPanel;
import io.github.dankoller.view.Vertex;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class Algorithm extends MouseAdapter {
    private ApplicationModel applicationModel;
    private ApplicationPanel applicationPanel;
    private String algorithmResult;
    private final AlgorithmPlayer algorithmPlayer = new AlgorithmPlayer(this);

    /**
     * Sets the application model for the algorithm.
     *
     * @param applicationModel The application model
     * @return The selected algorithm
     */
    public Algorithm setApplicationModel(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        return this;
    }

    /**
     * Handles mouse clicks on the application panel.
     *
     * @param event The mouse event
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        applicationPanel.getVertexAt(event.getPoint()).ifPresent(vertex -> {
            applicationModel.getModel().unselect();
            applicationModel.switchAlgorithmState(State.RUNNING);
            performAlgorithm(vertex);
        });
    }

    /**
     * Performs the algorithm on the given vertex. Implement this method to perform the algorithm
     *
     * @param vertex The vertex to start the algorithm from
     */
    protected abstract void performAlgorithm(Vertex vertex);

    /**
     * Propagates the result of the algorithm to the application model.
     */
    public void propagateResult() {
        applicationModel.propagateAlgorithmResult(algorithmResult);
    }

    /**
     * Stops the algorithm player.
     */
    public void stopPlaying() {
        if (algorithmPlayer.timer != null) {
            algorithmPlayer.timer.stop();
        }
    }

    /**
     * Set the algorithm result for the algorithm.
     *
     * @param algorithmResult The algorithm result
     */
    protected void setAlgorithmResult(String algorithmResult) {
        this.algorithmResult = algorithmResult;
    }

    /**
     * Set the application panel for the algorithm.
     *
     * @param applicationPanel The application panel the player is playing on
     */
    public void setApplicationPanel(ApplicationPanel applicationPanel) {
        this.applicationPanel = applicationPanel;
    }

    /**
     * Get the application model for the algorithm.
     *
     * @return The application model the player is playing on
     */
    protected ApplicationModel getApplicationModel() {
        return applicationModel;
    }

    /**
     * Get the algorithm player for the algorithm.
     *
     * @return The given algorithm player
     */
    protected AlgorithmPlayer getPlayer() {
        return algorithmPlayer;
    }
}
