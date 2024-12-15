package local.pkovalev.adventofcode.aoc2024.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MatrixPoint {
    int row;
    int col;

   public MatrixPoint moveTo(int row, int col) {
       this.row = row;
       this.col = col;
       return this;
   }

   public MatrixPoint shift(int dRow, int dCol) {
       this.row += dRow;
       this.col += dCol;
       return this;
   }

    public MatrixPoint newLeft() {
        return new MatrixPoint(row, col - 1);
    }
    public MatrixPoint newRight() {
        return new MatrixPoint(row, col + 1);
    }
    public MatrixPoint newAbove() {
        return new MatrixPoint(row - 1, col);
    }
    public MatrixPoint newBelow() {
        return new MatrixPoint(row + 1, col);
    }

    public boolean inside(int rowCount, int colCount) {
        return row >=0 && row < rowCount && col >= 0 && col < colCount;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", col, row);
    }
}
