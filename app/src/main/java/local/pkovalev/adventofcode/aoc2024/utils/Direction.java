package local.pkovalev.adventofcode.aoc2024.utils;

public enum Direction {
    UP(1 << 1),
    DOWN (1 << 2),
    RIGHT(1 << 3),
    LEFT(1 << 4);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public boolean isVertical() {
        return (this.value & V_MASK) != 0;
    }

    public boolean isHorizontal() {
        return (this.value & H_MASK) != 0;
    }

    public static final int  V_MASK = UP.value | DOWN.value;
    public static final int  H_MASK = LEFT.value | RIGHT.value;

    public Direction clockwise() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }

    public Direction counterclockwise() {
        return switch (this) {
            case UP -> LEFT;
            case RIGHT -> UP;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
        };
    }

}
