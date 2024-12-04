package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Slf4j
public class Day4 extends DayBase {

    int rows = 0;
    int cols = 0;
    char[][] matrix;
    @Override
    public void init() {
        var input = data;
        rows = input.size();
        cols = input.get(0).length();
        matrix = new char[rows][cols];
        parseData(input);
    }

    @Override
    public Long solvePartOne() {
        long result = 0L;
        result += searchRows();
        result += searchCols();
        result += searchRightDiagonal();
        result += searchLeftDiagonal();
        return result;
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        for(int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                if(matrix[row][col] == 'A' && isCross(row, col)) {
                    result.addAndGet(1);
                }
            }
        }
        return result.get();
    }

    void parseData(List<String> data) {
        AtomicInteger row = new AtomicInteger(0);
        data.forEach(line -> {
            matrix[row.get()] = line.toCharArray();
            row.addAndGet(1);
        });
    }


    int searchCols() {
        int counter = 0;
        for (int col = 0; col < cols; col++){
            for (int row = 0; row < rows - 3; row++) {
                if (isXMAS(matrix[row][col], matrix[row+1][col], matrix[row+2][col], matrix[row+3][col])) {
                    counter ++;
                }
            }
        }
        return counter;
    }
    int searchRows() {
        int counter = 0;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols - 3; col++) {
                if (isXMAS(matrix[row][col], matrix[row][col+1], matrix[row][col+2], matrix[row][col+3])) {
                    counter ++;
                }
            }
        }
        return counter;
    }

    int searchRightDiagonal() {
        int counter = 0;
        for (int row = 0; row < rows - 3; row++){
            for (int col = 0; col < cols - 3; col++) {
                if (isXMAS(matrix[row][col], matrix[row+1][col+1], matrix[row+2][col+2], matrix[row+3][col+3])) {
                    counter ++;
                }
            }
        }
        return counter;
    }

    int searchLeftDiagonal() {
        int counter = 0;
        for (int row = 0; row < rows - 3; row++){
            for (int col = 3; col < cols; col++) {
                if (isXMAS(matrix[row][col], matrix[row+1][col-1], matrix[row+2][col-2], matrix[row+3][col-3])) {
                    counter ++;
                }
            }
        }
        return counter;
    }

    boolean isXMAS(char x, char m, char a, char s) {
        return (x == 'X' && m == 'M' && a == 'A' && s == 'S')
        || (x == 'S' && m == 'A' && a == 'M' && s == 'X');
    }

    boolean isCross(int row, int col) {
        return(
            ((matrix[row-1][col-1] == 'M' && matrix[row+1][col+1] == 'S') || (matrix[row-1][col-1] == 'S' && matrix[row+1][col+1] == 'M')) &&
        ((matrix[row-1][col+1] == 'M' && matrix[row+1][col-1] == 'S') || (matrix[row-1][col+1] == 'S' && matrix[row+1][col-1] == 'M'))
        );
    }
}
