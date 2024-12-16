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
    Matrix<Character> map2;
    MatrixPoint robot = new MatrixPoint(0,0);
    String movements = "";

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        robot = map.findFirst('@').get();
        var steps = movements.toCharArray();
        for(char c : steps) {
            if (canMove(robot, c)) {
                move(robot, c);
            }
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
        map = map2;
        robot = map.findFirst('@').get();
        log.info(map.toString());
        var steps = movements.toCharArray();
        for(char c : steps) {
            if (canMove(robot, c)) {
                move(robot, c);
            }
        }
        log.info(map.toString());
        var boxes = map.findAll('[');
        boxes.forEach( index -> {
            result.addAndGet(index.getCol() + 100L * index.getRow());
        });
        return result.get();
    }

    void parseData(List<String> data) {
        List<String> mapData = new ArrayList<>();
        List<String> mapData2 = new ArrayList<>();
        int i = 0;
        for(; i < data.size(); i++) {
            if(data.get(i).isBlank()) {
                i++;
                break;
            }
            mapData.add(data.get(i));
            StringBuilder b = new StringBuilder();
            for ( char c : data.get(i).toCharArray()) {
                if ( c == '@') {
                    b.append("@.");
                }
                else if (c == 'O') {
                    b.append("[]");
                }
                else {
                    b.append(c).append(c);
                }
            }
            mapData2.add(b.toString());
        }

        map = asCharMatrix(mapData);
        map2 = asCharMatrix(mapData2);

        StringBuilder b = new StringBuilder();
        for(; i < data.size(); i++) {
            b.append(data.get(i));
        }
        movements = b.toString();
    }

    boolean canMove(MatrixPoint point, char direction) {
        var object = map.get(point);
        if(object.equals('.')) {
            return true;
        }
        if(object.equals('#')) {
            return false;
        }
        MatrixPoint newPoint = switch (direction) {
            case '>' -> point.newRight();
            case '<' -> point.newLeft();
            case 'v' -> point.newBelow();
            case '^' -> point.newAbove();
            default -> null;
        };
        if(object.equals('O') || object.equals('@')) {
            return canMove(newPoint, direction);
        }
        if(object.equals('[') || object.equals(']')) {
            if (direction == '>' || direction == '<') {
                return canMove(newPoint, direction);
            }
            else if (direction == '^' || direction == 'v') {
                var secondPoint = object.equals('[') ? newPoint.newRight() : newPoint.newLeft();
                return  canMove(newPoint, direction) && canMove(secondPoint, direction);
            }
        }
        log.error("Object: {}", object);
        throw new RuntimeException("Unexpected object");
    }

    void move(MatrixPoint point, char direction) {
        var object = map.get(point);
        if (object.equals('.')) {
            return;
        }
        if (object.equals('#')) {
            return;
        }
        MatrixPoint newPoint = switch (direction) {
            case '>' -> point.newRight();
            case '<' -> point.newLeft();
            case 'v' -> point.newBelow();
            case '^' -> point.newAbove();
            default -> null;
        };
        if (object.equals('O') || object.equals('@')) {
            move(newPoint, direction);
            map.set(newPoint, object);
            map.set(point, '.');
            if (object.equals('@')) {
                robot.setRow(newPoint.getRow());
                robot.setCol(newPoint.getCol());
            }
            return;
        }
        if (object.equals('[') || object.equals(']')) {
            if (direction == '>' || direction == '<') {
                move(newPoint, direction);
                map.set(newPoint, object);
                map.set(point, '.');
            } else if (direction == '^' || direction == 'v') {
                move(newPoint, direction);
                map.set(newPoint, object);
                map.set(point, '.');

                var secondPoint = object.equals('[') ? newPoint.newRight() : newPoint.newLeft();
                var secondObject = map.get(object.equals('[') ? point.newRight() : point.newLeft());
                move(secondPoint, direction);
                map.set(secondPoint, secondObject);
                map.set(object.equals('[') ? point.newRight() : point.newLeft(), '.');
            }
            return;
        }
        log.error("Object: {}", object);
        throw new RuntimeException("Unexpected object");
    }

}
