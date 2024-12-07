package local.pkovalev.adventofcode.aoc2024.days;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class Day7 extends DayBase {

    static class Equation {
        long result;
        List<Long> numbers;

        Equation(long res, List<Long> nums) {
            this.result = res;
            this.numbers = nums;
        }
    }

    ArrayList<Equation> equations = new ArrayList<>();

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        long result = 0L;
        result = equations.stream().filter(this::isValid).mapToLong(x -> x.result).sum();
        return result;
    }

    @Override
    public Long solvePartTwo() {
        long result = 0L;
        result = equations.stream().filter(this::isValidEx).mapToLong(x -> x.result).sum();
        return result;
    }

    void parseData(List<String> data) {
        data.forEach(line -> {
            var items = line.split(":");
            var nums = Arrays.stream(items[1].split(" ")).filter(Predicate.not(String::isBlank)).map(Long::parseLong).toList();
            equations.add(new Equation(Long.parseLong(items[0]), nums));
        });
    }

    boolean isValid(Equation equation) {
        return getAllResults(equation.numbers).stream().anyMatch( x -> x.compareTo(equation.result) == 0);
    }

    boolean isValidEx(Equation equation) {
        return getAllResultsEx(equation.numbers).stream().anyMatch( x -> x.compareTo(equation.result) == 0);
    }

    List<Long> getAllResults(List<Long> operands) {
        if (operands.size() == 1) {
            return List.of(operands.get(0));
        }
        List<Long> subResults = getAllResults(operands.subList(0, operands.size() - 1));
        ArrayList<Long> results = new ArrayList<>(subResults.size()*2);
        subResults.forEach( x -> {
            results.add(operands.get(operands.size() - 1) + x);
            results.add(operands.get(operands.size() - 1) * x);
        });
        return results;
    }

    List<Long> getAllResultsEx(List<Long> operands) {
        if (operands.size() == 1) {
            return List.of(operands.get(0));
        }
        List<Long> subResults = getAllResultsEx(operands.subList(0, operands.size() - 1));
        ArrayList<Long> results = new ArrayList<>(subResults.size()*3);
        subResults.forEach( x -> {
            results.add(operands.get(operands.size() - 1) + x);
            results.add(operands.get(operands.size() - 1) * x);
            results.add(Long.parseLong( x.toString() + operands.get(operands.size() - 1).toString()));
        });
        return results;
    }

}
