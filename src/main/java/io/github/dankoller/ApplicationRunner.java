package io.github.dankoller;

import io.github.dankoller.view.MainFrame;

import javax.swing.*;

public class ApplicationRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
