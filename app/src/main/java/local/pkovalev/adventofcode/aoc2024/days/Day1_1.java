package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Slf4j
public class Day1_1 implements DayInterface {

    @Override
    public Long solveRiddle() {
        long result = 0L;
        //var filename = getClass().getClassLoader().getResourceAsStream("data/" + this.getClass().getSimpleName() + "-test.txt");
        var filename = getClass().getClassLoader().getResourceAsStream("data/" + this.getClass().getSimpleName() + ".txt");

        List<Long> firstList = new ArrayList<>();
        List<Long> secondList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filename))) {
            for (String line; (line = reader.readLine()) != null; ) {
                var item = processString(line);
                firstList.add(item.first());
                secondList.add(item.second());
            }

            //result = partOne(firstList, secondList);
            result = partTwo(firstList, secondList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    long partOne(List<Long> firstList, List<Long> secondList) {
        long result = 0L;
        firstList.sort(Long::compareTo);
        secondList.sort(Long::compareTo);

        for (int i = 0; i < firstList.size(); i++) {
            result += Math.abs(firstList.get(i) - secondList.get(i));
        }
        return result;
    }

    long partTwo(List<Long> firstList, List<Long> secondList) {
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
