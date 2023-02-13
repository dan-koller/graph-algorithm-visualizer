package io.github.dankoller.model;

import java.util.EnumMap;
import java.util.Map;

public class AlgorithmModel {
    private final Map<State, String> stateTexts = new EnumMap<>(State.class);
    private State state;

    public AlgorithmModel() {
        this.state = State.SELECT_VERTEX;
        stateTexts.put(State.SELECT_VERTEX, "Please choose a starting vertex");
        stateTexts.put(State.RUNNING, "Please wait...");
    }

    public void setResultText(String result) {
        stateTexts.put(State.TERMINATED, result);
    }

    public String getDisplayedMessage() {
        return stateTexts.get(state);
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
