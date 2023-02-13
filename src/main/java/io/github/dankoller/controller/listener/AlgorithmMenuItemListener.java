package io.github.dankoller.controller.listener;

import io.github.dankoller.model.AlgorithmType;
import io.github.dankoller.model.ApplicationModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlgorithmMenuItemListener implements ActionListener {
    private final AlgorithmType algorithmType;
    private final ApplicationModel applicationModel;

    public AlgorithmMenuItemListener(AlgorithmType algorithmType, ApplicationModel applicationModel) {
        this.algorithmType = algorithmType;
        this.applicationModel = applicationModel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        applicationModel.startAlgorithm(algorithmType);
    }
}
