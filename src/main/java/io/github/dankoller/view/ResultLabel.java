package io.github.dankoller.view;

import io.github.dankoller.MainFrame;
import io.github.dankoller.controller.ApplicationModelListener;
import io.github.dankoller.model.AlgorithmModel;

import javax.swing.*;
import java.awt.*;

public class ResultLabel extends JLabel implements ApplicationModelListener {
    private static final Color FG_COLOR = Color.BLACK;
    public ResultLabel(String text) {
        super(text);
        setName("Display");
        setFont(new Font("Arial", Font.PLAIN, 18));
        setForeground(FG_COLOR);
    }

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
