package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Matrix;
import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Day18 extends DayBase {

    @Data
    @AllArgsConstructor
    static class Vertex {
        MatrixPoint point;
        int score;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vertex vertex = (Vertex) o;

            return point.equals(vertex.point);
        }

        @Override
        public int hashCode() {
            return point.getRow() * 10000 + point.getCol();
        }
    }

    Matrix<Character> map;
    List<MatrixPoint> badBlocks = null;

    HashMap<Integer, Vertex> vertices = new HashMap<>();
    ArrayList<Vertex> processed = new ArrayList<>();

    int lastRowIdx = 0;
    int lastColIdx = 0;


    @Override
    public void init() {
        lastRowIdx = 70;
        lastColIdx = 70;
        map = new Matrix<>(lastRowIdx + 1, lastColIdx + 1);
        map.fillWith('.');
        parseData(data);

    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        badBlocks.stream().limit(1024).forEach(matrixPoint -> map.set(matrixPoint, '#'));
        result.addAndGet(getFinishScore(1024, false).get());
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        int limit = 1024 + 1;

        for(; limit < badBlocks.size(); limit++) {
            map.set(badBlocks.get(limit - 1).getRow(), badBlocks.get(limit - 1).getCol(), '#');
            if (getFinishScore(limit, true).isEmpty()) {
                break;
            }
        }
        log.info("Blocked by {}", badBlocks.get(limit - 1));
        return result.get();
    }

    Optional<Integer> getFinishScore(int limit, boolean fastBreak) {

        vertices.clear();
        processed.clear();

        for (int row = 0; row < map.rows(); row++) {
            for (int col = 0; col < map.cols(); col++) {
                if(map.get(row, col) != '#') {
                    Vertex v = new Vertex(new MatrixPoint(row, col), Integer.MAX_VALUE);
                    vertices.put(v.hashCode(), v);
                }
            }
        }
        Vertex start = new Vertex(new MatrixPoint(0, 0), 0);
        vertices.put(start.hashCode(), start);

        while(!vertices.isEmpty()) {
            var v = vertices.values().stream().min(Comparator.comparingInt(Vertex::getScore)).get();
            if ( fastBreak && v.point.getRow() == lastRowIdx && v.point.getCol() == lastColIdx) {
                return Optional.of(v.score);
            }
            if (v.score == Integer.MAX_VALUE) {
                break;
            }
            getConnected(v).forEach( next -> {
                var realNext = vertices.get(next.hashCode());
                if(realNext != null) {
                    realNext.setScore(Math.min(realNext.score, v.score + 1));
                }
            });
            processed.add(v);
            vertices.remove(v.hashCode());
        }

        return processed.stream().filter(x -> x.point.getRow() == lastRowIdx && x.point.getCol() == lastColIdx).findFirst().map(Vertex::getScore);
    }

    void parseData(List<String> data) {
        ArrayList<MatrixPoint> bb = new ArrayList<>(data.size());
        data.forEach( line -> {
            var subs = line.split(",");
            bb.add(new MatrixPoint(Integer.parseInt(subs[1]), Integer.parseInt(subs[0])));
        });
        badBlocks = bb;
    }

    List<Vertex> getConnected(Vertex v) {
        List<MatrixPoint> points = List.of(
            v.point.newAbove(),
            v.point.newBelow(),
            v.point.newLeft(),
            v.point.newRight()
        );
        ArrayList<Vertex> ret = new ArrayList<>();
        points.forEach( p-> {
            if(p.inside(map.rows(), map.cols()) && map.get(p) != '#') {
                ret.add(new Vertex(p, Integer.MAX_VALUE));
            }
        });
        return ret;
    }

}
