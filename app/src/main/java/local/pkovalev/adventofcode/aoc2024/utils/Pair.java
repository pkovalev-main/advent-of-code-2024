package local.pkovalev.adventofcode.aoc2024.utils;

import lombok.Getter;

public class Pair<T, U> {
    T t;
    U u;

    public Pair( T first, U second) {
        t = first;
        u = second;
    }

    public T first() {
        return t;
    }

    public U second() {
        return u;
    }
}
