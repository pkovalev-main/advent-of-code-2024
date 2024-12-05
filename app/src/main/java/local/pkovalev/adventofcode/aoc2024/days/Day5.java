package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class Day5 extends DayBase {

    List<Pair<Integer, Integer>> rules = null;
    List<List<Integer>> sequences = null;

    @Override
    public void init() {
        rules = new ArrayList<>();
        sequences = new ArrayList<>();
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        long result = 0L;
        result = sequences.stream()
            .map(this::getScores)
            .filter(this::isAscending)
            .mapToInt(seq -> seq.get(seq.size() / 2 ).second())
            .sum();
        return result;
    }

    @Override
    public Long solvePartTwo() {
        long result = 0L;
        result = sequences.stream()
            .map(this::getScores)
            .filter(Predicate.not(this::isAscending))
            .map( x-> x.stream().sorted(Comparator.comparing(Pair::first)).collect(Collectors.toList()))
            .mapToInt(seq -> seq.get(seq.size() / 2 ).second())
            .sum();
        return result;
    }

    void parseData(List<String> data) {
        AtomicBoolean isRules = new AtomicBoolean(true);
        data.forEach(line -> {
            if(line.isBlank()) {
                isRules.set(false);
            }
            else {
                if(isRules.get()) {
                    var items = line.split("\\|");
                    var pair = new Pair<>(Integer.parseInt(items[0]), Integer.parseInt(items[1]));
                    rules.add(pair);
                }
                else {
                    var items = line.split(",");
                    sequences.add(Arrays.stream(items).map(Integer::parseInt).collect(Collectors.toList()));
                }
            }
        });
    }

    List<Pair<Integer, Integer>> getScores(List<Integer> seq) {
        ArrayList<Pair<Integer, Integer>> scores = new ArrayList<>();
        for(int i = 0; i < seq.size(); i++) {
            int pos = i;
            var itemScore = rules.stream()
                .filter(x -> x.second().equals(seq.get(pos)))
                .filter( x -> seq.contains(x.first()))
                .mapToInt(Pair::first)
                .sum();
            scores.add(new Pair<>(itemScore, seq.get(i)));
        }
        return scores;
    }

    boolean isAscending(List<Pair<Integer, Integer>> seq) {
        for (int i = 1; i < seq.size(); i++) {
            if(seq.get(i-1).first() > seq.get(i).first()) {
                return false;
            }
        }
        return true;
    }
}
