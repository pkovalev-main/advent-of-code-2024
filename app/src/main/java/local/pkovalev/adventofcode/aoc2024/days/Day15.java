package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Matrix;
import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Day15 extends DayBase {

    ArrayList<MatrixPoint> boxes = new ArrayList<>();
    Matrix<Character> map;
    MatrixPoint robot = new MatrixPoint(0,0);
    String movements = "";

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        var steps = movements.toCharArray();
        for(char c : steps) {
            moveIfPossible(robot, c);
        }
        log.info(map.toString());
        var boxes = map.findAll('O');
        boxes.forEach( index -> {
            result.addAndGet(index.getCol() + 100L * index.getRow());
        });
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        return result.get();
    }

    void parseData(List<String> data) {
        List<String> mapData = new ArrayList<>();
        int i = 0;
        for(; i < data.size(); i++) {
            if(data.get(i).isBlank()) {
                i++;
                break;
            }
            mapData.add(data.get(i));
        }

        map = asCharMatrix(mapData);

        robot = map.findFirst('@').get();


        StringBuilder b = new StringBuilder();
        for(; i < data.size(); i++) {
            b.append(data.get(i));
        }
        movements = b.toString();
    }

    boolean moveIfPossible(MatrixPoint point, char direction) {
        var object = map.get(point);
        if(object.equals('.')) {
            return true;
        }
        if(object.equals('#')) {
            return false;
        }
        if(object.equals('O') || object.equals('@')) {
            MatrixPoint newPoint = null;
            switch (direction) {
                case '>':
                    newPoint = point.newRight();
                    break;
                case '<':
                    newPoint = point.newLeft();
                    break;
                case 'v':
                    newPoint = point.newBelow();
                    break;
                case '^':
                    newPoint = point.newAbove();
                    break;
            }
            if(moveIfPossible(newPoint, direction)) {
                map.set(newPoint, object);
                map.set(point, '.');
                if(object.equals('@')) {
                    robot.setRow(newPoint.getRow());
                    robot.setCol(newPoint.getCol());
                }
                return true;
            }
            return false;
        }
        log.error("Object: {}", object);
        throw new RuntimeException("Unexpected object");
    }

}
