package io.github.dankoller.controller;

import io.github.dankoller.model.ApplicationModel;
import io.github.dankoller.model.Mode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeMenuItemListener implements ActionListener {
    private final Mode mode;
    private final ApplicationModel model;

    public ModeMenuItemListener(Mode mode, ApplicationModel model) {
        this.mode = mode;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setMode(mode);
    }
}
