package local.pkovalev.adventofcode.aoc2024.utils;

import lombok.Getter;

public class NamedPair<T, U> {
    @Getter
    String name;
    T t;
    U u;

    public NamedPair(String name, T first, U second) {
        t = first;
        u = second;
        this.name = name;
    }

    public T first() {
        return t;
    }

    public U second() {
        return u;
    }
}
