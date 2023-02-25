package io.github.dankoller.model;

/**
 * The mode of the application.
 */
public enum Mode {
    NONE("None"),
    RESET_MODE("Add a Vertex"),
    ADD_VERTEX("Add a Vertex"),
    ADD_EDGE("Add an Edge"),
    REMOVE_VERTEX("Remove a Vertex"),
    REMOVE_EDGE("Remove an Edge");

    private final String name;
    public static final Mode START_MODE = Mode.ADD_VERTEX;

    Mode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
