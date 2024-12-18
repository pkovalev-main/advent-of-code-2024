package local.pkovalev.adventofcode.aoc2024.days;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Day17 extends DayBase {

    long regA;
    long regB;
    long regC;
    int iPtr;
    String instructionsString;
    List<Integer> instructions;
    StringBuilder output = new StringBuilder();

    @Override
    public void init() {
        parseData(data);
        iPtr = 0;
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        while(iPtr < instructions.size() - 1) {
            if(exec(instructions.get(iPtr), instructions.get(iPtr + 1))) {
                iPtr += 2;
            }
        }
        log.info("Part 1: {}", output.deleteCharAt(output.length()-1).toString());
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        long newA = 0;
        newA = 6498099L << 24; //Found by starting with newA = 0 at subIndex = 15
        long initB = regB;
        long initC = regC;
        int subIndex = 1;
        while(true) {
            iPtr = 0;
            output = new StringBuilder();
            regA = newA++;
            regB = initB;
            regC = initC;
            while (iPtr < instructions.size() - 1) {
                if (exec(instructions.get(iPtr), instructions.get(iPtr + 1))) {
                    iPtr += 2;
                }
            }
            var out = output.deleteCharAt(output.length() - 1).toString();
            var sub = instructionsString.substring(instructionsString.length() - subIndex);
            if( out.equals(sub)) {
                log.info("{} at {} ({})", sub, Long.toBinaryString(newA - 1), newA - 1);
                subIndex += 2;
            }

            if(out.equals(instructionsString)) {
                break;
            }
        }
        result.addAndGet(newA-1);
        return result.get();
    }

    void parseData(List<String> data) {
        regA = Integer.parseInt(data.get(0).split(": ")[1]);
        regB = Integer.parseInt(data.get(1).split(": ")[1]);
        regC = Integer.parseInt(data.get(2).split(": ")[1]);


        instructionsString = data.get(4).split(": ")[1];
        instructions = Arrays.stream(instructionsString.split(",")).map(Integer::parseInt).toList();
    }

    boolean exec(int code, int arg) {
        boolean shiftIPtr = true;
        switch (code) {
            case 0:
                regA = regA >> getComboOperand(arg);
                break;
            case 1:
                regB = regB ^ arg;
                break;
            case 2:
                regB = getComboOperand(arg) % 8;
                break;
            case 3:
                if (regA != 0) {
                    iPtr = arg;
                    shiftIPtr = false;
                }
                break;
            case 4:
                regB = regC ^ regB;
                break;
            case 5:
                output.append(getComboOperand(arg)%8).append(',');;
                break;
            case 6:
                regB = regA >> getComboOperand(arg);
                break;
            case 7:
                regC = regA >> getComboOperand(arg);
                break;
        }
        return shiftIPtr;
    }

    long getComboOperand(int arg) {
        if (arg <= 3) return arg;
        return switch (arg) {
            case 4 -> regA;
            case 5 -> regB;
            case 6 -> regC;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + arg);
        };
    }

}
