package io.github.dankoller.controller.listener;

import io.github.dankoller.model.AlgorithmType;
import io.github.dankoller.model.ApplicationModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlgorithmMenuItemListener implements ActionListener {
    private final AlgorithmType algorithmType;
    private final ApplicationModel applicationModel;

    /**
     * Handles the selection of an algorithm in the menu bar.
     *
     * @param algorithmType    The selected algorithm
     * @param applicationModel The model of the application
     */
    public AlgorithmMenuItemListener(AlgorithmType algorithmType, ApplicationModel applicationModel) {
        this.algorithmType = algorithmType;
        this.applicationModel = applicationModel;
    }

    /**
     * Start the selected algorithm.
     *
     * @param event The event that triggered the action
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        applicationModel.startAlgorithm(algorithmType);
    }
}
