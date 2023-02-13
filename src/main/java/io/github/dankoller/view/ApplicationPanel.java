package io.github.dankoller.view;

import io.github.dankoller.controller.algorithm.Algorithm;
import io.github.dankoller.controller.GraphClickListener;
import io.github.dankoller.controller.ApplicationModelListener;
import io.github.dankoller.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Optional;

public class ApplicationPanel extends JPanel implements ApplicationModelListener {
    private final GraphModel model;
    private Vertex selectedVertex = null;
    private transient Algorithm runningAlgorithm = null;
    private Mode mode = Mode.START_MODE;
    public ApplicationPanel(GraphModel model) {
        super(null);
        this.model = model;
        setName("Graph");
        addMouseListener(new GraphClickListener(this));
    }

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

    @Override
    public void onModelChangeSetAlgorithm(Algorithm algorithm) {
        this.runningAlgorithm = algorithm;
        algorithm.setApplicationPanel(this);
        addMouseListener(algorithm);
    }

    @Override
    public void onModelChangeSetAlgorithmState(AlgorithmModel algorithmModel) {
        if (algorithmModel.getState() == State.STOPPED) {
            removeMouseListener(runningAlgorithm);
            runningAlgorithm.stopPlaying();
            runningAlgorithm = null;
        }
    }

    public Optional<Vertex> getSelectedVertex() {
        return Optional.ofNullable(selectedVertex);
    }

    public void setSelectedVertex(Vertex vertex) {
        selectedVertex = vertex;
        if (vertex != null) {
            model.selectVertex(vertex);
        }
    }

    public Optional<Vertex> getVertexAt(Point point) {
        return Optional.ofNullable(getComponentAt(point) instanceof Vertex v && v.isInside(point) ? v : null);
    }

    public void addVertex(String label, Point position) {
        Vertex vertex = new Vertex(label, getBackground());
        vertex.setLocation(position);
        model.addVertex(vertex);
        add(vertex);
        revalidate();
        repaint();
    }

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

    private static Point getLabelPosition(Point start, Point end) {
        Point mid = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
        Point position;
        int del = Vertex.getVertexRadius() / 10;
        if ((end.y - start.y) * (end.x - start.x) < 0) {
            position = new Point(mid.x + del, mid.y + del);
        } else {
            position = new Point(mid.x - 3 * del, mid.y + del);
        }
        return position;
    }

    public void removeVertexWithAssociateEdges(Vertex vertex) {
        var componentsToRemove = model.removeVertexWithEdges(vertex);
        componentsToRemove.forEach(this::remove);
        repaint();
    }

    /**
     * delegate remove request to the model, which returns all the swing components, the panel must remove.
     */
    public void removeEdge(Edge edge) {
        var componentsToRemove = model.removeEdge(edge);
        componentsToRemove.forEach(this::remove);
        repaint();
    }

    @Override
    public void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Vertex.getVertexColor());
        g2d.setStroke(new BasicStroke(Vertex.getVertexRadius() / 10f));
        model.getEdges().forEach(e -> {
            g2d.setColor(e.isSelected() ? Vertex.getVertexSelectedColor() : Vertex.getVertexColor());
            g2d.draw(new Line2D.Float(e.getStart(), e.getEnd()));
        });
        super.paintChildren(g2d);
    }

    public Mode getMode() {
        return mode;
    }
}
