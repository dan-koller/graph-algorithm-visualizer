package io.github.dankoller.model;

import java.util.EnumMap;
import java.util.Map;

public class AlgorithmModel {
    private final Map<State, String> stateTexts = new EnumMap<>(State.class);
    private State state;

    /**
     * Upon creating a new instance, the state is set to SELECT_VERTEX and the corresponding message is set.
     */
    public AlgorithmModel() {
        this.state = State.SELECT_VERTEX;
        stateTexts.put(State.SELECT_VERTEX, "Please choose a starting vertex");
        stateTexts.put(State.RUNNING, "Please wait...");
    }

    /**
     * Display the result of the algorithm when it has finished.
     *
     * @param result The result of the algorithm.
     */
    public void setResultText(String result) {
        stateTexts.put(State.TERMINATED, result);
    }

    /**
     * Get the message that is currently displayed.
     *
     * @return The message that is currently displayed.
     */
    public String getDisplayedMessage() {
        return stateTexts.get(state);
    }

    /**
     * Set the state of the algorithm.
     *
     * @param state The state of the algorithm.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Get the state of the algorithm.
     *
     * @return The state of the algorithm.
     */
    public State getState() {
        return state;
    }
}
