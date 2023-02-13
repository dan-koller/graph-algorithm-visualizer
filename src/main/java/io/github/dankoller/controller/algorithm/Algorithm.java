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

    public Algorithm setApplicationModel(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        return this;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        applicationPanel.getVertexAt(event.getPoint()).ifPresent(vertex -> {
            applicationModel.getModel().unselect();
            applicationModel.switchAlgorithmState(State.RUNNING);
            performAlgorithm(vertex);
        });
    }

    // Implement this method to perform the algorithm
    protected abstract void performAlgorithm(Vertex vertex);

    public void propagateResult() {
        applicationModel.propagateAlgorithmResult(algorithmResult);
    }

    public void stopPlaying() {
        if (algorithmPlayer.timer != null) {
            algorithmPlayer.timer.stop();
        }
    }

    // Getters and setters
    public void setAlgorithmResult(String algorithmResult) { // may be protected
        this.algorithmResult = algorithmResult;
    }

    public void setApplicationPanel(ApplicationPanel applicationPanel) {
        this.applicationPanel = applicationPanel;
    }

    public ApplicationModel getApplicationModel() {
        return applicationModel;
    }

    public AlgorithmPlayer getPlayer() {
        return algorithmPlayer;
    }
}
