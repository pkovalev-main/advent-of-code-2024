package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Slf4j
public class Day1 extends DayBase {

    List<Long> firstList = new ArrayList<>();
    List<Long> secondList = new ArrayList<>();

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        return partOne();
    }

    @Override
    public Long solvePartTwo() {
        return partTwo();
    }

    void parseData(List<String> data) {
        data.forEach(line -> {
            var item = processString(line);
            firstList.add(item.first());
            secondList.add(item.second());
        });
    }

    long partOne() {
        long result = 0L;
        firstList.sort(Long::compareTo);
        secondList.sort(Long::compareTo);

        for (int i = 0; i < firstList.size(); i++) {
            result += Math.abs(firstList.get(i) - secondList.get(i));
        }
        return result;
    }

    long partTwo() {
       AtomicLong result = new AtomicLong(0L);

       firstList.forEach(x -> {
           result.addAndGet(secondList.stream().filter(y -> y.equals(x)).count() * x);
       });
       return result.get();
    }

    Pair<Long, Long> processString(String input) {
        var split = Arrays.stream(input.split(" ")).filter(Predicate.not(String::isBlank)).toList();
        return new Pair<>(Long.parseLong(split.get(0)), Long.parseLong(split.get(1)));
    }

}
