package local.pkovalev.adventofcode.aoc2024.days;

import lombok.extern.slf4j.Slf4j;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Day8 extends DayBase {

    static class Antenna {
        char name;
        Point loc;
        Antenna(char name, Point loc) {
            this.name = name;
            this.loc = loc;
        }
    }

    char[][] map = null;
    ArrayList<Antenna> antennas = new ArrayList<>();
    HashSet<Character> uniqueNames = new HashSet<>();

    @Override
    public void init() {
        parseData(data);
        findAntennas();
    }

    @Override
    public Long solvePartOne() {
        long result = 0L;
        HashSet<Point> points = new HashSet<>();
        uniqueNames.forEach( name -> {
            var allWithName = antennas.stream().filter(x -> x.name == name).toList();
            allWithName.forEach(first -> {
                allWithName.forEach(second -> {
                    points.addAll(getResonansePoints(first, second));
                });
            });
        });
        result = points.stream().filter(this::insideMap).count();
        return result;
    }

    @Override
    public Long solvePartTwo() {
        long result = 0L;
        HashSet<Point> points = new HashSet<>();
        uniqueNames.forEach( name -> {
            var allWithName = antennas.stream().filter(x -> x.name == name).toList();
            allWithName.forEach(first -> {
                allWithName.forEach(second -> {
                    points.addAll(getResonansePointsEx(first, second));
                });
            });
        });
        result = points.stream().filter(this::insideMap).count();
        return result;
    }

    List<Point> getResonansePoints(Antenna a1, Antenna a2) {
        int deltaX = a1.loc.x - a2.loc.x;
        int deltaY = a1.loc.y - a2.loc.y;
        if(deltaX == 0 && deltaY == 0) {
            return List.of();
        }
        Point p1 = new Point(a1.loc);
        p1.translate(deltaX, deltaY);
        Point p2 = new Point(a2.loc);
        p2.translate(-deltaX, -deltaY);
        return List.of(p1, p2);
    }

    List<Point> getResonansePointsEx(Antenna a1, Antenna a2) {
        int deltaX = a1.loc.x - a2.loc.x;
        int deltaY = a1.loc.y - a2.loc.y;
        if(deltaX == 0 && deltaY == 0) {
            return List.of();
        }
        Point p1 = new Point(a1.loc);
        Point p2 = new Point(a2.loc);
        ArrayList<Point> points = new ArrayList<>();
        while (insideMap(p1)) {
            points.add(new Point(p1));
            p1.translate(deltaX, deltaY);
        }
        while (insideMap(p2)) {
            points.add(new Point(p2));
            p2.translate(-deltaX, -deltaY);
        }
        return points;
    }

    boolean insideMap(Point point) {
        return point.getY() >= 0 && point.getY() < map.length
            && point.getX() >= 0 && point.getX() < map[0].length;
    }

    void parseData(List<String> data) {
        map = new char[data.size()][data.get(0).length()];
        AtomicInteger index = new AtomicInteger(0);
        data.forEach(line -> {
            map[index.getAndIncrement()] = line.toCharArray();
        });
    }

    void findAntennas() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] != '.') {
                    antennas.add(new Antenna(map[row][col], new Point(col, row)));
                    uniqueNames.add(map[row][col]);
                }
            }
        }
    }
}
