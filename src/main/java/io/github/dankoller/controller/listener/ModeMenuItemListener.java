package io.github.dankoller.controller.listener;

import io.github.dankoller.model.ApplicationModel;
import io.github.dankoller.model.Mode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeMenuItemListener implements ActionListener {
    private final Mode mode;
    private final ApplicationModel model;

    /**
     * Create a new mode menu item listener.
     *
     * @param mode  Mode to set
     * @param model Application model
     */
    public ModeMenuItemListener(Mode mode, ApplicationModel model) {
        this.mode = mode;
        this.model = model;
    }

    /**
     * When an action is performed, set the mode of the application model.
     *
     * @param e The event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.setMode(mode);
    }
}
