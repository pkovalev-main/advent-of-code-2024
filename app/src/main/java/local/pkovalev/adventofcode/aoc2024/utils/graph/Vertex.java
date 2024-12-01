package local.pkovalev.adventofcode.aoc2023.utils.graph;

import local.pkovalev.adventofcode.aoc2023.utils.Direction;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;

@Data
public class Vertex {
    @Data
    public static class VertexId {
        Point coordinates;
        Direction direction;
        int vectorLength;
    }

    int priority = Integer.MAX_VALUE;
    VertexId id;
    int weight;
    ArrayList<Vertex> connections;
    Vertex prev = null;
    boolean done = false;

    public Vertex(VertexId id) {
        this.id = id;
        connections = new ArrayList<>();
    }

    public Point pointsTo() {
        int moveX = id.getDirection().isHorizontal() ? (id.getDirection() == Direction.RIGHT ? id.vectorLength : -id.vectorLength) : 0;
        int moveY = id.getDirection().isVertical() ? (id.getDirection() == Direction.DOWN ? id.vectorLength : -id.vectorLength) : 0;
        return new Point(id.coordinates).moveX(moveX).moveY(moveY);
    }

    public static Comparator<Vertex> priorityComparator() {
        return new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                return o1.priority - o2.priority;
            }
        };
    }
}
