package local.pkovalev.adventofcode.aoc2023.utils.graph;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Point {
    public int x = 0;
    public int y = 0;

    public Point(Point other) {
        x = other.x;
        y = other.y;
    }

    public Point moveX(int increment) {
        x += increment;
        return this;
    }
    public Point moveY(int increment) {
        y += increment;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
