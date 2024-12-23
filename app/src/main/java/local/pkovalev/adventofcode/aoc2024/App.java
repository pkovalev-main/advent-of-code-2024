/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package local.pkovalev.adventofcode.aoc2024;

import local.pkovalev.adventofcode.aoc2024.days.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class App
{
    public static void main( String[] args )
    {
        DayInterface day = new Day23();
        day.init();

        var timeStart = Instant.now();
        var result = day.solvePartOne();
        var timeEnd = Instant.now();
        var interval = timeEnd.toEpochMilli() - timeStart.toEpochMilli();
        log.info("Part I done in {}.{} seconds", interval / 1000, String.format("%03d", interval % 1000));
        log.info("Result: [{}]", result);

        timeStart = Instant.now();
        result = day.solvePartTwo();
        timeEnd = Instant.now();
        interval = timeEnd.toEpochMilli() - timeStart.toEpochMilli();
        log.info("Part II done in {}.{} seconds", interval / 1000, String.format("%03d", interval % 1000));
        log.info("Result: [{}]", result);
    }
}

