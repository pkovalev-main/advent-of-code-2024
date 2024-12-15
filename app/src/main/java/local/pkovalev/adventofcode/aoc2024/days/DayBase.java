package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Matrix;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public abstract class DayBase implements DayInterface {

    ArrayList<String> testData = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();

    DayBase() {
        String className = this.getClass().getSimpleName();
        String fnTest = String.format("%s-test.txt", className);
        String fn = String.format("%s.txt", className);
        var filenameTest = getClass().getClassLoader().getResourceAsStream(String.format("data/%s", fnTest));
        var filename = getClass().getClassLoader().getResourceAsStream(String.format("data/%s", fn));

        if (filenameTest != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(filenameTest))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    testData.add(line);
                }

                if(testData.get(testData.size() - 1).isBlank()) {
                    testData.remove(testData.size() - 1);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (filename != null) {
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
    }

    Matrix<Character> asCharMatrix(List<String> data) {
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
