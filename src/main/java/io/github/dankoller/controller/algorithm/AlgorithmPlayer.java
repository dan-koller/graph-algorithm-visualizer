package io.github.dankoller.controller.algorithm;

import io.github.dankoller.model.EdgeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

/**
 * This class is used to play the animation of the algorithm
 */
public class AlgorithmPlayer implements ActionListener {
    private static final int DELAY = 700; // ms
    protected Timer timer;
    private Queue<EdgeModel> playQueue;
    private final Algorithm algorithm;

    /**
     * Creates a new AlgorithmPlayer to visualize the given algorithm.
     *
     * @param algorithm The algorithm to play
     */
    public AlgorithmPlayer(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Plays the animation of the algorithm on the graph.
     *
     * @param playQueue The queue of edges to play
     */
    protected void play(Queue<EdgeModel> playQueue) {
        this.playQueue = playQueue;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Upon receiving an event, the next edge in the queue is selected. If the queue is empty, the timer is stopped
     * and the algorithm is finished.
     *
     * @param e The event to be processed
     */
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
