package local.pkovalev.adventofcode.aoc2024.utils;

import lombok.Getter;
import lombok.Setter;

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

    public void setFirst(T first) {
        this.t = first;
    }

    public U second() {
        return u;
    }

    public void setSecond(U second) {
        this.u = second;
    }
}
