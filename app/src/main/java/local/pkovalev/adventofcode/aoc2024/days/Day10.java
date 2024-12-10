package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Day10 extends DayBase {

    ArrayList<MatrixPoint> starts = new ArrayList<>();

    int mapRows = 0;
    int mapCols = 0;
    int[][] map = null;

    @Override
    public void init() {
        parseData(data);
        findStarts();
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        starts.forEach(start -> {
            HashSet<MatrixPoint> uniquePoints = new HashSet<>();
            var list = pathsForPoint(start);
            var full = list.stream().filter(x -> x.size() == 10).toList();
            full.forEach(x ->{
                uniquePoints.add(x.get(x.size()-1));
            });
            result.addAndGet(uniquePoints.size());
        });
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        starts.forEach(start -> {
            var list = pathsForPoint(start);
            var full = list.stream().filter(x -> x.size() == 10).toList();
            result.addAndGet(full.size());
        });
        return result.get();
    }

    void parseData(List<String> data) {
        mapRows = data.size();
        mapCols = data.get(0).length();
        map = new int[mapRows][mapCols];
        AtomicInteger currentRow = new AtomicInteger(0);
        data.forEach(row -> {
            var arr = row.chars().map(x -> x - 48).toArray();
            map[currentRow.getAndIncrement()] = arr;
        });
    }

    void findStarts() {
        for (int row = 0; row < mapRows; row++) {
            for (int col = 0; col < mapCols; col++) {
                if(map[row][col] == 0) {
                    starts.add(new MatrixPoint(row, col));
                }
            }
        }
    }

    ArrayList<ArrayList<MatrixPoint>> pathsForPoint(MatrixPoint point) {
        var steps = getPossibleWays(point);
        ArrayList<ArrayList<MatrixPoint>> ret = new ArrayList<>();
        steps.forEach( step -> {
            var list = pathsForPoint(step);
            list.forEach(item -> {
                item.add(0, point);
            });
            ret.addAll(list);
        });

        if(steps.isEmpty()) {
            ret.add(new ArrayList<>(List.of(point)));
        }
        return ret;
    }

    List<MatrixPoint> getPossibleWays(MatrixPoint point) {
        return Stream.of(point.newAbove(), point.newBelow(), point.newLeft(), point.newRight())
            .filter(x -> x.inside(mapRows, mapCols))
            .filter(x -> map[x.getRow()][x.getCol()] - map[point.getRow()][point.getCol()] == 1)
            .toList();
    }
}
