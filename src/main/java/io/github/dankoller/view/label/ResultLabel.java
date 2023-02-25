package io.github.dankoller.view.label;

import io.github.dankoller.view.MainFrame;
import io.github.dankoller.controller.listener.ApplicationModelListener;
import io.github.dankoller.model.AlgorithmModel;

import javax.swing.*;
import java.awt.*;

public class ResultLabel extends JLabel implements ApplicationModelListener {
    private static final Color FG_COLOR = Color.BLACK;

    /**
     * Creates a new label for displaying the result of an algorithm.
     *
     * @param text The text to display
     */
    public ResultLabel(String text) {
        super(text);
        setName("Display");
        setFont(new Font("Arial", Font.PLAIN, 18));
        setForeground(FG_COLOR);
    }

    /**
     * Update the background color of the panel according to the current state of the algorithm. If the algorithm is
     * running, the background color is set to light gray to show the result of the algorithm. If the algorithm has
     * stopped, the background color is set to the default color of the panel for a better user experience.
     *
     * @param algorithmModel The model of the algorithm
     */
    @Override
    public void onModelChangeSetAlgorithmState(AlgorithmModel algorithmModel) {
        switch (algorithmModel.getState()) {
            case SELECT_VERTEX -> getParent().setBackground(Color.LIGHT_GRAY);
            case STOPPED -> getParent().setBackground(MainFrame.PANEL_COLOR);
            default -> {/* no action */}
        }
        setText(algorithmModel.getDisplayedMessage());
    }
}
