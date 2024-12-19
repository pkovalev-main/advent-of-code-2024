package local.pkovalev.adventofcode.aoc2024.days;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class Day19 extends DayBase {

    List<String> chunks;
    List<String> patterns;

    HashMap<String, Long> cache = new HashMap<>();

    @Override
    public void init() {
        parseData(data);

    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        result.addAndGet(patterns.stream().filter( chunk -> {
            var combinations = combinations(chunk);
            return combinations > 0;
        }).count());
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        result.addAndGet(patterns.stream().mapToLong(this::combinations).sum());
        return result.get();
    }

    void parseData(List<String> data) {
        chunks = Arrays.stream(data.get(0).split(", ")).toList();
        patterns = IntStream.range(2, data.size()).mapToObj(data::get).collect(Collectors.toList());
    }

    long combinations(String pattern) {
        if(pattern.isBlank()) {
            return 1;
        }
        if(cache.containsKey(pattern)) {
            return cache.get(pattern);
        }
        List<String> beginWith = chunks.stream().filter(pattern::startsWith).toList();
        if (beginWith.isEmpty()) {
            return 0;
        }

        long combinations = 0;
        for (String s : beginWith) {
            var toAdd = combinations(pattern.substring(s.length()));
            combinations += toAdd;
        }
        cache.put(pattern, combinations);
        return combinations;
    }

}
