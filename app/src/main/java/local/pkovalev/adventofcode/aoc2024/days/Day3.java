package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Day3 extends DayBase {


     static final String SUM_PATTERN = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)";
    static final String COMBINED_PATTERN = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|do\\(\\)|don't\\(\\)";

    List<String> input;

    boolean isDo = true;
    @Override
    public void init() {
        input = data;
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        input.forEach( x -> {
            result.addAndGet(processString(x));
        });
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        input.forEach( x -> {
            result.addAndGet(processString2(x));
        });
        return result.get();
    }

    long processString(String data) {
        int ret = 0;
        Pattern p = Pattern.compile(SUM_PATTERN);
        Matcher m = p.matcher(data);
        while (m.find()) {
            log.info(m.group(0));
            ret += Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
        }
        return ret;
    }

    long processString2(String data) {
        int ret = 0;
        Pattern p = Pattern.compile(COMBINED_PATTERN);
        Matcher m = p.matcher(data);
        while (m.find()) {
            if(m.group(0).startsWith("don")) {
                isDo = false;
            } else if (m.group(0).startsWith("do(")) {
                isDo = true;
            }
            else {
                if (isDo) {
                    ret += Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
                }
            }
        }
        return ret;
    }
}
