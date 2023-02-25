package io.github.dankoller.view.label;

import io.github.dankoller.controller.listener.ApplicationModelListener;
import io.github.dankoller.model.Mode;

import javax.swing.*;
import java.awt.*;

public class StatusLabel extends JLabel implements ApplicationModelListener {
    private static final Color FOREGROUND_COLOR = Color.WHITE;

    /**
     * Creates a new label for displaying the current mode.
     *
     * @param modeName The name of the current mode
     */
    public StatusLabel(String modeName) {
        super("Current Mode -> %s".formatted(modeName));
        setName("Mode");
        setFont(new Font("Arial", Font.PLAIN, 18));
        setForeground(FOREGROUND_COLOR);
    }

    /**
     * Update the text of the label according to the current mode.
     *
     * @param mode The current mode
     */
    @Override
    public void onModelChangeSetMode(Mode mode) {
        setText("Current Mode -> %s".formatted(mode.getName()));
    }
}
