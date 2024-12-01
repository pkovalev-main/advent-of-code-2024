package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2023.utils.Matrix;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.IntStream;

@Slf4j
public abstract class DayBase implements DayInterface {

    ArrayList<String> data = new ArrayList<>();

    DayBase() {
        String className = this.getClass().getName();
        String fn = String.format("input%s.txt", className.substring(className.indexOf("Day") + 3, className.indexOf('_')));
        var filename = getClass().getClassLoader().getResourceAsStream(String.format("data/%s", fn));
        if (filename == null) {
            throw new RuntimeException(String.format("File %s doesn't exist", fn));
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filename))) {
            for (String line; (line = reader.readLine()) != null; ) {
                data.add(line);
            }

            if(data.get(data.size() - 1).isBlank()) {
                data.remove(data.size() - 1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Matrix<Character> dataAsCharMatrix() {
        Matrix<Character> ret = new Matrix<>(data.size(), data.get(0).length());
        IntStream.range(0, data.size()).forEach(row -> {
            for(int col = 0; col < data.get(row).length(); col++) {
                ret.set(row, col, data.get(row).charAt(col));
            }
        });
        return ret;
    }

    int[][] dataAsIntMatrix() {
        int[][] ret = new int[data.size()][data.get(0).length()];
        IntStream.range(0, data.size()).forEach(row -> {
            for(int col = 0; col < data.get(row).length(); col++) {
                ret[row][col] = Integer.parseInt(data.get(row).substring(col, col+1));
            }
        });
        return ret;
    }
}