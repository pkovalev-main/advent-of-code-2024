package local.pkovalev.adventofcode.aoc2023.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Matrix<T> {
    private final ArrayList<ArrayList<T>> data;

    public Matrix(int rows, int cols) {
        this.data = new ArrayList<>(rows);
        IntStream.range(0, cols).forEach(index -> {
            var row = new ArrayList<T>(cols);
            IntStream.range(0, rows).forEach(i -> row.add(null));
            data.add(row);
        });
    }

    public T get(int row, int col) {
        return data.get(row).get(col);
    }

    public void set(int row, int col, T value) {
        data.get(row).set(col, value);
    }

    public List<T> getRow(int index) {
        return new ArrayList<>(data.get(index));
    }

    public List<T> getColumn(int index) {
        ArrayList<T> ret = new ArrayList<>();
        for (ArrayList<T> datum : data) {
            ret.add(datum.get(index));
        }
        return ret;
    }

    public void addRow(List<T> row) {
        data.add(new ArrayList<>(row));
    }

    public void addRow(int pos, List<T> row) {
        data.add(pos, new ArrayList<>(row));
    }

    public int cols() {
        return data.get(0).size();
    }

    public int rows() {
        return data.size();
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        data.forEach( row -> {
            StringBuilder rowBuilder = new StringBuilder();
            row.forEach(item -> {
                if(rowBuilder.length() != 0) rowBuilder.append(' ');
                rowBuilder.append(item);
            });
            if(ret.length() != 0) ret.append('\n');
            ret.append(rowBuilder);
        });
        return ret.toString();
    }
}
