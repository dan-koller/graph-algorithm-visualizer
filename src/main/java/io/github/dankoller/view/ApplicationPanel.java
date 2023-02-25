package io.github.dankoller.view;

import io.github.dankoller.controller.algorithm.Algorithm;
import io.github.dankoller.controller.listener.GraphClickListener;
import io.github.dankoller.controller.listener.ApplicationModelListener;
import io.github.dankoller.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Optional;
import java.util.Set;

public class ApplicationPanel extends JPanel implements ApplicationModelListener {
    private final GraphModel model;
    private Vertex selectedVertex = null;
    private transient Algorithm runningAlgorithm = null;
    private Mode mode = Mode.START_MODE;

    /**
     * Create the panel for the graph and set the model.
     *
     * @param model The model representing the graph
     */
    public ApplicationPanel(GraphModel model) {
        super(null);
        this.model = model;
        setName("Graph");
        addMouseListener(new GraphClickListener(this));
    }

    /**
     * Update the model according to the current mode.
     *
     * @param mode The current mode
     */
    @Override
    public void onModelChangeSetMode(Mode mode) {
        this.mode = mode;
        model.unselect();
        selectedVertex = null;
        if (mode == Mode.RESET_MODE) {
            this.mode = Mode.START_MODE;
            model.clear();
            removeAll();
            repaint();
        }
    }

    /**
     * Update the model according to the current algorithm and add a mouse listener to the panel.
     *
     * @param algorithm The selected algorithm
     */
    @Override
    public void onModelChangeSetAlgorithm(Algorithm algorithm) {
        this.runningAlgorithm = algorithm;
        algorithm.setApplicationPanel(this);
        addMouseListener(algorithm);
    }

    /**
     * Handle the state change of the algorithm.
     *
     * @param algorithmModel The model representing the algorithm
     */
    @Override
    public void onModelChangeSetAlgorithmState(AlgorithmModel algorithmModel) {
        if (algorithmModel.getState() == State.STOPPED) {
            removeMouseListener(runningAlgorithm);
            try {
                runningAlgorithm.stopPlaying();
            } catch (NullPointerException npe) {
                System.err.println("No algorithm running: " + npe.getMessage());
            } finally {
                runningAlgorithm = null;
            }
        }
    }

    /**
     * Get the selected vertex or null if no vertex is selected.
     *
     * @return The selected vertex or null
     */
    public Optional<Vertex> getSelectedVertex() {
        return Optional.ofNullable(selectedVertex);
    }

    /***
     * Set the vertex of the graph as selected.
     * @param vertex The vertex to be selected
     */
    public void setSelectedVertex(Vertex vertex) {
        selectedVertex = vertex;
        if (vertex != null) {
            model.selectVertex(vertex);
        }
    }

    /**
     * Get a vertex of a certain point in the graph.
     *
     * @param point The point to be checked
     * @return The vertex at the point or null if no vertex is at the point
     */
    public Optional<Vertex> getVertexAt(Point point) {
        return Optional.ofNullable(getComponentAt(point) instanceof Vertex v && v.isInside(point) ? v : null);
    }

    /**
     * Add a vertex to the graph, update the model and repaint the panel to render the newly added vertex.
     *
     * @param label    The label of the vertex
     * @param position The position of the vertex
     */
    public void addVertex(String label, Point position) {
        Vertex vertex = new Vertex(label, getBackground());
        vertex.setLocation(position);
        model.addVertex(vertex);
        add(vertex);
        revalidate();
        repaint();
    }

    /**
     * Add an edge to the graph, update the model and repaint the panel to render the newly added edge.
     *
     * @param weight The weight of the edge
     * @param from   The vertex the edge starts from
     * @param to     The vertex the edge ends at
     */
    public void addEdge(String weight, Vertex from, Vertex to) {
        Edge edge = new Edge(from, to);
        edge.setBounds(new Rectangle(new Point((from.getCenter().x + to.getCenter().x) / 2 - 10,
                (from.getCenter().y + to.getCenter().y) / 2 - 10), new Dimension(20, 20)));
        add(edge);
        Edge reversedEdge = new Edge(to, from);
        add(reversedEdge);
        JLabel weightLabel = createWeightLabel(weight, from, to);
        add(weightLabel);
        model.addEdge(edge, reversedEdge, weightLabel);
        revalidate();
        repaint();
    }

    /**
     * Helper method to create a weight label for an edge.
     *
     * @param weight The weight of the edge
     * @param from   The vertex the edge starts from
     * @param to     The vertex the edge ends at
     * @return The weight label
     */
    private JLabel createWeightLabel(String weight, Vertex from, Vertex to) {
        Point position = getLabelPosition(from.getCenter(), to.getCenter());
        JLabel label = new JLabel(weight);
        label.setName("EdgeLabel <%s -> %s>".formatted(from.getLabel(), to.getLabel()));
        label.setForeground(Vertex.getVertexColor());
        int fontSize = Vertex.getVertexRadius() * 2 / 5;
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setBounds(position.x, position.y, fontSize * 3 / 2, fontSize * 3 / 2);
        return label;
    }

    /**
     * Helper method to calculate the position of the weight label.
     *
     * @param start The start vertex
     * @param end   The end vertex
     * @return The position of the weight label
     */
    private static Point getLabelPosition(Point start, Point end) {
        Point mid = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
        Point position;
        int offset = Vertex.getVertexRadius() / 10;
        if ((end.y - start.y) * (end.x - start.x) < 0) {
            position = new Point(mid.x + offset, mid.y + offset);
        } else {
            position = new Point(mid.x - 3 * offset, mid.y + offset);
        }
        return position;
    }

    /**
     * Remove a vertex and all edges associated with it from the graph.
     *
     * @param vertex The vertex to be removed
     */
    public void removeVertexWithAssociateEdges(Vertex vertex) {
        Set<Component> componentsToRemove = model.removeVertexWithEdges(vertex);
        componentsToRemove.forEach(this::remove);
        repaint();
    }

    /**
     * Remove an edge from the graph.
     *
     * @param edge The edge to be removed
     */
    public void removeEdge(Edge edge) {
        Set<Component> componentsToRemove = model.removeEdge(edge);
        componentsToRemove.forEach(this::remove);
        repaint();
    }

    /**
     * Get the mode of the application panel.
     *
     * @return The mode of the application panel
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * This method handles the painting and rendering of the graph. It draws the edges and then calls the super method
     * to render the vertices.
     *
     * @param g The graphics object (cast to Graphics2D)
     */
    @Override
    public void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(Vertex.getVertexRadius() / 10f));
        model.getEdges().forEach(e -> {
            g2d.setColor(e.isSelected() ? Vertex.getVertexSelectedColor() : Vertex.getVertexColor());
            g2d.draw(new Line2D.Float(e.getStart(), e.getEnd()));
        });
        super.paintChildren(g2d);
    }
}
