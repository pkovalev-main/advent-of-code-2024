package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class Day11 extends DayBase {
    ArrayList<Pair<Long, Long>> numbersWithCounter = new ArrayList<>();

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        List<Pair<Long, Long>> arr = numbersWithCounter;
        for (int i = 0; i < 25; i++) {
            arr = evolve(arr);
            arr = compact(arr);
        }
        arr.forEach( x -> {
            result.addAndGet(x.second());
        });
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        List<Pair<Long, Long>> arr = numbersWithCounter;
        for (int i = 0; i < 75; i++) {
            arr = evolve(arr);
            arr = compact(arr);
        }
        arr.forEach( x -> {
            result.addAndGet(x.second());
        });
        return result.get();
    }

    void parseData(List<String> data) {
        var items = data.get(0).split(" ");
        for (String item : items) {
            numbersWithCounter.add(new Pair<>(Long.parseLong(item), 1L));
        }
    }

    List<Pair<Long, Long>> evolve(List<Pair<Long, Long>> seq) {
        ArrayList<Pair<Long, Long>> evolved = new ArrayList<>();
        seq.forEach( x -> {
            if(x.first() == 0) {
                evolved.add(new Pair<>(1L, x.second()));
            }
            else if(x.first().toString().length() % 2 == 0){
                var str = x.first().toString();
                var first = str.substring(0, str.length()/2);
                var second = str.substring(str.length()/2);
                evolved.add(new Pair<>(Long.parseLong(first), x.second()));
                evolved.add(new Pair<>(Long.parseLong(second), x.second()));
            }
            else {
                evolved.add(new Pair<>(x.first()*2024, x.second()));
            }
        });
        return evolved;
    }

    List<Pair<Long, Long>> compact(List<Pair<Long, Long>> in) {
        var unique = in.stream().map(Pair::first).collect(Collectors.toSet());
        var ret = new ArrayList<Pair<Long, Long>>();

        unique.forEach(x -> {
            var total= in.stream().filter(y -> Objects.equals(y.first(), x)).mapToLong(Pair::second).sum();
            ret.add(new Pair<>(x, total));
        });
        return ret;
    }
}
