package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

// This class is used to play the animation of the algorithm
public class AlgorithmPlayer implements ActionListener {
    private static final int DELAY = 700; // ms
    protected Timer timer;
    private Queue<EdgeModel> playQueue;
    private final Algorithm algorithm;

    public AlgorithmPlayer(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    protected void play(Queue<EdgeModel> playQueue) {
        this.playQueue = playQueue;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (playQueue.isEmpty()) {
            timer.stop();
            algorithm.propagateResult();
        } else {
            algorithm.getApplicationModel().getModel().selectEdgeAndNeighborVertex(playQueue.remove());
        }
    }
}
