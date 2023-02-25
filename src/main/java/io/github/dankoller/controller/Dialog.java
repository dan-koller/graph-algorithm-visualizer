package io.github.dankoller.controller;

import io.github.dankoller.model.Mode;
import io.github.dankoller.view.ApplicationPanel;

import javax.swing.*;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class Dialog {
    private static final Map<Mode, Predicate<String>> VALIDATORS = Map.of(
            Mode.ADD_VERTEX, input -> input == null || input.length() == 1 && !input.isBlank(),
            Mode.ADD_EDGE, input -> input == null || input.matches("[+-]?\\d{1,2}")
    );
    private static final Map<Mode, String> DIALOG_TITLES = Map.of(
            Mode.ADD_VERTEX, "Vertex",
            Mode.ADD_EDGE, "Edge"
    );
    private static final Map<Mode, String> DIALOG_MESSAGES = Map.of(
            Mode.ADD_VERTEX, "Enter the Vertex ID (Should be 1 char):",
            Mode.ADD_EDGE, "Enter Weight:"
    );

    /**
     * This method handles the creation of a dialog box for the user to enter a label for a vertex or edge.
     * The label either represents the ID of a vertex or the weight of an edge.
     *
     * @param applicationPanel The ApplicationPanel
     * @return The label entered by the user
     */
    public Optional<String> getLabelFromDialog(ApplicationPanel applicationPanel) {
        String label;
        Mode currentMode = applicationPanel.getMode();
        do {
            label = JOptionPane.showInputDialog(applicationPanel,
                    DIALOG_MESSAGES.get(currentMode),
                    DIALOG_TITLES.get(currentMode),
                    JOptionPane.QUESTION_MESSAGE);
        } while (!VALIDATORS.get(currentMode).test(label));
        return Optional.ofNullable(label);
    }
}