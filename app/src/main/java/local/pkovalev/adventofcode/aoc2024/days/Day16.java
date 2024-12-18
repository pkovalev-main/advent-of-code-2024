package local.pkovalev.adventofcode.aoc2024.days;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import local.pkovalev.adventofcode.aoc2024.utils.Direction;
import local.pkovalev.adventofcode.aoc2024.utils.Matrix;
import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Day16 extends DayBase {

    @Data
    static class Vertex {
        MatrixPoint point;
        Direction direction;
        int score = Integer.MAX_VALUE;
        boolean visited = false;
        HashSet<Vertex> parents = new HashSet<>();

        public Vertex(MatrixPoint nextPoint, Direction direction) {
            this.point = nextPoint;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Vertex vertex = (Vertex) o;
            return Objects.equals(point, vertex.point) && direction == vertex.direction;
        }

        @Override
        public int hashCode() {
            return point.getRow() * 10000 + point.getCol() * 10 + direction.hashCode();
        }
    }

    HashMap<Integer, Vertex> vertices = new HashMap<>();
    Matrix<Character> map;
    MatrixPoint start;

    @Override
    public void init() {
        parseData(data);
        start = map.findFirst('S').get();
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        Vertex startVertex = new Vertex(start, Direction.RIGHT);
        startVertex.setScore(0);
        vertices.putIfAbsent(startVertex.hashCode(), startVertex);

        while (vertices.values().stream().anyMatch(vertex -> !vertex.visited)) {
            var lowest = getLowestScoreVertex();
            var connected = getConnectedVertices(lowest);

            connected.forEach((k, v) -> {
                if (k.point.inside(map.rows(), map.cols()) && map.get(k.point) != '#') {
                    vertices.putIfAbsent(k.hashCode(), k);
                    k = vertices.get(k.hashCode());
                    if (lowest.getScore() + v <= k.score) {
                        if(lowest.getScore() + v < k.score) {
                            k.parents.clear();
                        }
                        k.setScore(lowest.getScore() + v);
                        k.parents.add(lowest);
                    }
                }
            });
            lowest.setVisited(true);
        }

        var minE = vertices.values().stream().filter(vertex -> map.get(vertex.point) == 'E').min(Comparator.comparing(Vertex::getScore));
        result.addAndGet(minE.get().score);
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        var minE = vertices.values().stream().filter(vertex -> map.get(vertex.point) == 'E').min(Comparator.comparing(Vertex::getScore));
        var ends = vertices.values().stream().filter(vertex -> map.get(vertex.point) == 'E').filter(v -> v.score == minE.get().score).toList();

        HashSet<Vertex> visited = new HashSet<>();
        ends.forEach( end -> {
            visited.addAll(getParents(end));
        });

        visited.forEach(vertex -> {
            map.set(vertex.point, '0');
                });
        log.info("Map: {}", map.toString());

        result.addAndGet(visited.stream().map(Vertex::getPoint).collect(Collectors.toSet()).size() + 1);
        return result.get();
    }

    HashSet<Vertex> getParents(Vertex vertex) {
       HashSet<Vertex> parents = new HashSet<>(vertex.parents);
       vertex.parents.forEach(v -> {
           parents.addAll(getParents(v));
       });
       return parents;
    }

    void parseData(List<String> data) {
        map = asCharMatrix(data);
    }

    Vertex getLowestScoreVertex() {
        return vertices.values().stream().filter(Predicate.not(Vertex::isVisited)).min(Comparator.comparingInt(a -> a.score)).get();
    }

    Map<Vertex, Integer> getConnectedVertices(Vertex vertex) {
        var nextPoint = switch (vertex.direction) {
            case UP -> vertex.point.newAbove();
            case DOWN -> vertex.point.newBelow();
            case LEFT -> vertex.point.newLeft();
            case RIGHT -> vertex.point.newRight();
        };
        return  Map.of(
                new Vertex(nextPoint, vertex.getDirection()), 1,
                new Vertex(vertex.point, vertex.getDirection().clockwise()), 1000,
                new Vertex(vertex.point, vertex.getDirection().counterclockwise()), 1000
                );
    }

}
