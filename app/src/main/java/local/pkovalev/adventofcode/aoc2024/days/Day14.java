package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Slf4j
public class Day14 extends DayBase {

    ArrayList<Robot> robots = new ArrayList<>();

    int maxRows;
    int maxCols;

    @Data
    @AllArgsConstructor
    static class Robot {
        MatrixPoint pos;
        long vCol;
        long vRow;
    }

    @Override
    public void init() {
        maxCols = 11;
        maxRows = 7;

        maxCols = 101;
        maxRows = 103;
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        for (int i = 0; i < 100; i++) {
            step();
        };
        result.set(calcScore());
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        init();
        AtomicLong result = new AtomicLong(0L);
        int step = 0;

        while (!hasTree(false)) {
            step();
            step++;
        }
        ;
        hasTree(true);
        result.set(step);
        return result.get();
    }

    void parseData(List<String> data) {
        robots.clear();
        Pattern p = Pattern.compile("p=([-\\d]+),([-\\d]+) v=([-\\d]+),([-\\d]+)");
        data.forEach(line -> {
            Matcher m = p.matcher(line);
            m.find();
            var robot = new Robot(new MatrixPoint(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1))), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
            robots.add(robot);
        });
    }

    void step() {
        robots.forEach( robot -> {
            var newCol = robot.pos.getCol() + robot.vCol;
            var newRow = robot.pos.getRow() + robot.vRow;
            if(newCol >= maxCols) {
                newCol = newCol % maxCols;
            }
            else if (newCol < 0) {
                newCol = maxCols + newCol;
            }
            if(newRow >= maxRows) {
                newRow = newRow % maxRows;
            }
            else if (newRow < 0) {
                newRow = maxRows + newRow;
            }
            robot.pos.setCol((int) newCol);
            robot.pos.setRow((int) newRow);
        });
    }

    long calcScore() {
        long[][] scores = new long[2][2];
        robots.stream()
            .filter(robot -> (robot.pos.getCol() != (maxCols/2)) && (robot.pos.getRow() != (maxRows/2)))
            .forEach(robot -> {
            scores[robot.pos.getRow() <= maxRows/2 ? 0 : 1][robot.pos.getCol() <= maxCols/2 ? 0 : 1] += 1;
        });
        return scores[0][0] * scores[1][0] * scores[0][1] * scores[1][1];
    }

    boolean hasTree(boolean show) {
        char[][] matrix = new char[maxRows][maxCols];

        robots.forEach( r ->{
            matrix[r.pos.getRow()][r.pos.getCol()] = 'X';
        });

        for(int i = 0; i <  maxRows; i++) {
            StringBuilder b = new StringBuilder();
            for(int j = 0; j < maxCols; j++) {
                if (matrix[i][j] != 'X') {
                    matrix[i][j] = '.';
                }
                b.append(matrix[i][j]);
            }
            if (show) {
                log.info(b.toString());
            }
        }
        return findDiagonalOfN(matrix);
    }

    boolean findDiagonalOfN(char[][] field) {
        int limit = 10;
       boolean found = false;
        for(int i = limit-1; i <  maxRows && !found; i++) {
            for(int j = 0; j < maxCols - limit && !found; j++) {
                boolean hasEmpty = false;
                for(int k = 0; k < limit - 1; k++) {
                    if(field[i - k][j + k] != 'X') {
                        hasEmpty = true;
                        break;
                    }
                }
                if(!hasEmpty) {
                    found = true;
                }
            }
        }
        return found;
    }


}
