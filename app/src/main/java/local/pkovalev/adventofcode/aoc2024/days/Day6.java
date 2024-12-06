package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class Day6 extends DayBase {

    char[][] field = null;
    int[] initialGuardPos = new int[2];
    int[] guardPos = new int[2];
    int[] increment = new int[2];

    int[] obstacle = new int[2];

    HashSet<Integer> trace = new HashSet<>();

    @Override
    public void init() {
        parseData(data);
        increment[0] = -1;
        increment[1] = 0;
    }

    @Override
    public Long solvePartOne() {
        long result = 0L;
        guardPos[0] = initialGuardPos[0];
        guardPos[1] = initialGuardPos[1];
        while (move(true, false)) {
        }

        for(int row = 0; row < field.length; row++) {
            for(int col = 0; col < field[row].length; col++) {
                if(field[row][col] == '0') {
                    result++;
                }
            }
        }
        return result;
    }

    @Override
    public Long solvePartTwo() {
        long result = 0L;

        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                if(field[row][col] != '0' || (row == initialGuardPos[0] && col == initialGuardPos[1])) {
                    continue;
                }
                guardPos[0] = initialGuardPos[0];
                guardPos[1] = initialGuardPos[1];
                increment[0] = -1;
                increment[1] = 0;
                obstacle[0] = row;
                obstacle[1] = col;
                trace = new HashSet<>();
                while (move(false, true)) {
                    var hash = getCurrentPosHash();
                    if (trace.contains(hash)) {
                        result += 1;
                        break;
                    }
                    trace.add(hash);
                }
            }
        }
        return result;
    }

    void parseData(List<String> data) {
        field = new char[data.size()][data.get(0).length()];
        AtomicInteger i = new AtomicInteger(0);
        data.forEach(line -> {
            field[i.getAndIncrement()] = line.toCharArray();
            int guardCol = line.indexOf('^');
            if (guardCol != -1) {
                initialGuardPos[0] = i.get() - 1;
                initialGuardPos[1] = guardCol;
            }
        });
    }

    boolean move(boolean mark, boolean extraObstacle) {
        if(mark) {
            field[guardPos[0]][guardPos[1]] = '0';
        }
        if(isStepOutside()) {
            return false;
        }
        if(field[guardPos[0] + increment[0]][guardPos[1] + increment[1]] == '#') {
            rotate();
        }
        else if(extraObstacle && guardPos[0]+increment[0] == obstacle[0] && guardPos[1]+increment[1] == obstacle[1]) {
            rotate();
        }
        else {
            guardPos[0] = guardPos[0]+increment[0];
            guardPos[1] = guardPos[1]+increment[1];
        }
        return true;
    }

    Integer getCurrentPosHash() {
        return guardPos[0]*10000 + guardPos[1]*10 + 5 + 2*increment[0] + increment[1];
    }

    boolean isStepOutside() {
        return !((guardPos[0] + increment[0] >=0 && guardPos[0] + increment[0] < field.length)
            && (guardPos[1] + increment[1] >=0 && guardPos[1] + increment[1] < field[0].length));
    }

    void rotate() {
        if(increment[0] == 0 && increment[1] == 1) {
            increment[0] = 1;
            increment[1] = 0;
        }
        else if(increment[0] == 1 && increment[1] == 0) {
            increment[0] = 0;
            increment[1] = -1;
        }
        else if(increment[0] == 0 && increment[1] == -1) {
            increment[0] = -1;
            increment[1] = 0;
        }
        else if(increment[0] == -1 && increment[1] == 0) {
            increment[0] = 0;
            increment[1] = 1;
        }
    }

}
