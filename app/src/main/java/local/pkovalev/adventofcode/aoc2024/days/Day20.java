package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Matrix;
import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;
import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class Day20 extends DayBase {

    Matrix<Character> map;
    ArrayList<MatrixPoint> track = new ArrayList<>();

    @Override
    public void init() {
        parseData(data);
    }

    void parseData(List<String> data) {
        map = asCharMatrix(data);
        var start = map.findFirst('S').get();
        track.add(start);
        var next = findNext(start);
        while(next != null) {
            track.add(next);
            next = findNext(next);
        }
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        ArrayList<Pair<MatrixPoint, MatrixPoint>> pairs = new ArrayList<>();
        for (int i = 0; i < track.size() - 1; i++) {
            for (int j = i+1; j < track.size(); j++) {
                var first = track.get(i);
                var second = track.get(j);
                if ((Math.abs(first.getRow() - second.getRow()) == 2 && first.getCol() == second.getCol())
                    || (Math.abs(first.getCol() - second.getCol()) == 2 && first.getRow() == second.getRow())) {
                    pairs.add(new Pair<>(first, second));
                }
            }
        }
        var shortcuts = pairs.stream().filter(pair -> Math.abs(track.indexOf(pair.first()) - track.indexOf(pair.second())) - 2 >= 100).count();
        result.addAndGet(shortcuts);
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        ArrayList<Pair<MatrixPoint, MatrixPoint>> pairs = new ArrayList<>();
        for (int i = 0; i < track.size() - 1; i++) {
            for (int j = i+1; j < track.size(); j++) {
                var first = track.get(i);
                var second = track.get(j);
                if ((Math.abs(first.getRow() - second.getRow()) + Math.abs(first.getCol() - second.getCol()) <= 20)) {
                    pairs.add(new Pair<>(first, second));
                }
            }
        }
        var shortcuts = pairs.stream()
            .filter(pair -> {
            var distance = Math.abs(track.indexOf(pair.first()) - track.indexOf(pair.second()));
            var cheatLength = Math.abs(pair.first().getCol() - pair.second().getCol()) + Math.abs(pair.first().getRow() - pair.second().getRow());
            return distance - cheatLength >= 100;
            })
            .count();
        result.addAndGet(shortcuts);
        return result.get();
    }

    MatrixPoint findNext(MatrixPoint start) {
        map.set(start, 'x');
        for ( var d : start.newNeighbours()) {
            if (map.get(d) == '.' || map.get(d) == 'E') {
                return d;
            }
        }
        return null; //last point
    }

}
