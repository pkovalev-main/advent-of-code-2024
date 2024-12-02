package local.pkovalev.adventofcode.aoc2024.days;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Slf4j
public class Day2 extends DayBase {

    List<List<Integer>> reports = new ArrayList<>();
    int MIN_DIFF = 1;
    int MAX_DIFF = 3;

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);

        reports.forEach( report -> {
            if(isValid(report)) {
                result.addAndGet(1);
            }
        });
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        reports.forEach(report -> {
            var isBadAsc = isValidAsc(report);
            if (isBadAsc != -1) {
                var copy1 = new ArrayList<>(report);
                var copy2 = new ArrayList<>(report);
                copy1.remove(isBadAsc);
                copy2.remove(isBadAsc+1);
                if(isValid(copy1) || isValid(copy2)) {
                    isBadAsc = -1;
                }
            }
            var isBadDesc = isValidDesc(report);
            if (isBadDesc != -1) {
                var copy1 = new ArrayList<>(report);
                var copy2 = new ArrayList<>(report);
                copy1.remove(isBadDesc);
                copy2.remove(isBadDesc+1);
                if(isValid(copy1) || isValid(copy2)) {
                    isBadDesc = -1;
                }
            }

            if (isBadAsc == -1 || isBadDesc == -1) {
                result.addAndGet(1);
            }

        });
        return result.get();
    }

    boolean isValid(List<Integer> report) {
        return (isValidAsc(report) == -1 || isValidDesc(report) == -1);
    }

    void parseData(List<String> data) {
        data.forEach(line -> {
            var item = processString(line);
            reports.add(item);
        });
    }

    List<Integer> processString(String input) {
        var split = Arrays.stream(input.split(" "))
            .filter(Predicate.not(String::isBlank))
            .map(Integer::parseInt).toList();
        return split;
    }

    int isValidAsc(List<Integer> seq) {
        int ret = -1;
        for(int i = 0; i < seq.size() - 1; i++) {
            int diff = seq.get(i+1) - seq.get(i);
            if(diff < MIN_DIFF || diff > MAX_DIFF ) {

                ret = i;
                break;
            }
        }
        return ret;
    }
    int isValidDesc(List<Integer> seq) {
        int ret = -1;
        for(int i = 0; i < seq.size() - 1; i++) {
            int diff = seq.get(i) - seq.get(i+1);
            if(diff < MIN_DIFF || diff > MAX_DIFF ) {
                ret = i;
                break;
            }
        }
        return ret;
    }

}
