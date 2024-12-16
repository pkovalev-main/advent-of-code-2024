package local.pkovalev.adventofcode.aoc2024.utils;

import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class Matrix<T> {
    private final ArrayList<ArrayList<T>> data;

    public Matrix(int rows, int cols) {
        this.data = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++ ) {
            this.data.add(new ArrayList<T>(cols));
            for (int col = 0; col < cols; col ++) {
                this.data.get(row).add(null);
            }
        }
    }


    public Optional<MatrixPoint> findFirst(T toFind) {
        for (int row = 0; row < data.size(); row++ ) {
            for (int col = 0; col < data.get(0).size(); col ++) {
                if(data.get(row).get(col).equals(toFind)) {
                    return Optional.of(new MatrixPoint(row, col));
                }
            }
        }
        return Optional.empty();
    }

    public List<MatrixPoint> findAll(T toFind) {
        ArrayList<MatrixPoint> ret = new ArrayList<>();
        for (int row = 0; row < data.size(); row++ ) {
            for (int col = 0; col < data.get(0).size(); col ++) {
                if (data.get(row).get(col).equals(toFind)) {
                    ret.add(new MatrixPoint(row, col));
                }
            }
        }
        return ret;
    }

    public List<MatrixPoint> findAll(T toFind, Comparator<T> cmp) {
        ArrayList<MatrixPoint> ret = new ArrayList<>();
        for (int row = 0; row < data.size(); row++ ) {
            for (int col = 0; col < data.get(0).size(); col ++) {
                if (cmp.compare(data.get(row).get(col), toFind) == 0) {
                    ret.add(new MatrixPoint(row, col));
                }
            }
        }
        return ret;
    }

    public T get(MatrixPoint point) {
        return get(point.getRow(), point.getCol());
    }

    public T get(int row, int col) {
        return data.get(row).get(col);
    }

    public void set(MatrixPoint point, T val) {
        set(point.getRow(), point.getCol(), val);
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
                if(!rowBuilder.isEmpty()) rowBuilder.append(' ');
                rowBuilder.append(item);
            });
            ret.append('\n');
            ret.append(rowBuilder);
        });
        return ret.toString();
    }
}
