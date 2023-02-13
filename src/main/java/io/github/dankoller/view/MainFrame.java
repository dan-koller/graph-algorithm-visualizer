package io.github.dankoller.view;

import io.github.dankoller.controller.listener.AlgorithmMenuItemListener;
import io.github.dankoller.controller.listener.ModeMenuItemListener;
import io.github.dankoller.model.AlgorithmType;
import io.github.dankoller.model.ApplicationModel;
import io.github.dankoller.model.Mode;
import io.github.dankoller.view.label.ResultLabel;
import io.github.dankoller.view.label.StatusLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    public static final Color PANEL_COLOR = Color.BLACK;
    private static final String TITLE = "Graph-Algorithms Visualizer";
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private final ApplicationModel applicationModel = new ApplicationModel();

    public MainFrame() {
        super(TITLE);
        setName(TITLE);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        add(createStatusPanel(), BorderLayout.NORTH);
        add(createGraphContainer(), BorderLayout.CENTER);
        add(createDisplayPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private Component createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        statusPanel.setBackground(PANEL_COLOR);
        StatusLabel statusLabel = new StatusLabel(Mode.START_MODE.getName());
        applicationModel.registerListener(statusLabel);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

    private Container createGraphContainer() {
        ApplicationPanel graphPanel = new ApplicationPanel(applicationModel.getModel());
        graphPanel.setBackground(PANEL_COLOR);
        applicationModel.registerListener(graphPanel);
        return graphPanel;
    }

    private Component createDisplayPanel() {
        JPanel displayPanel = new JPanel(new FlowLayout());
        displayPanel.setBackground(PANEL_COLOR);
        ResultLabel displayLabel = new ResultLabel(" ");
        applicationModel.registerListener(displayLabel);
        displayPanel.add(displayLabel);
        return displayPanel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setName("MenuBar");
        menuBar.add(createFileMenu());
        menuBar.add(createModeMenu());
        menuBar.add(createAlgorithmMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        var fileMenu = new JMenu("File");
        fileMenu.setName("File");
        fileMenu.add(createMenuItem("New", e -> applicationModel.requestResetGraph()));
        fileMenu.add(createMenuItem("Exit",
                e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));
        return fileMenu;
    }

    private JMenu createModeMenu() {
        var modeMenu = new JMenu("Mode");
        modeMenu.setName("Mode");
        modeMenu.add(createMenuItem(Mode.ADD_VERTEX));
        modeMenu.add(createMenuItem(Mode.ADD_EDGE));
        modeMenu.add(createMenuItem(Mode.REMOVE_VERTEX));
        modeMenu.add(createMenuItem(Mode.REMOVE_EDGE));
        modeMenu.add(createMenuItem(Mode.NONE));
        return modeMenu;
    }

    private JMenu createAlgorithmMenu() {
        var algorithmMenu = new JMenu("Algorithms");
        algorithmMenu.setName("Algorithms");
        algorithmMenu.add(createMenuItem(AlgorithmType.DEPTH_FIRST));
        algorithmMenu.add(createMenuItem(AlgorithmType.BREADTH_FIRST));
        algorithmMenu.add(createMenuItem(AlgorithmType.DIJKSTRA_ALGORITHM));
        algorithmMenu.add(createMenuItem(AlgorithmType.PRIM_ALGORITHM));
        return algorithmMenu;
    }

    private JMenuItem createMenuItem(String name, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setName(name);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    private JMenuItem createMenuItem(Mode mode) {
        JMenuItem menuItem = new JMenuItem(mode.getName());
        menuItem.setName(mode.getName());
        menuItem.addActionListener(new ModeMenuItemListener(mode, applicationModel));
        return menuItem;
    }

    private Component createMenuItem(AlgorithmType algorithmType) {
        JMenuItem menuItem = new JMenuItem(algorithmType.getAlgorithmName());
        menuItem.setName(algorithmType.getAlgorithmName());
        menuItem.addActionListener(new AlgorithmMenuItemListener(algorithmType, applicationModel));
        return menuItem;
    }
}