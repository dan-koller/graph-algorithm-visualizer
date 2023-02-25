package io.github.dankoller.model;

/**
 * A model for a vertex and the length of the route to it.
 *
 * @param vertex      The vertex
 * @param routeLength The length of the route to the vertex
 */
public record VertexAndRouteModel(VertexModel vertex, int routeLength) {
}
