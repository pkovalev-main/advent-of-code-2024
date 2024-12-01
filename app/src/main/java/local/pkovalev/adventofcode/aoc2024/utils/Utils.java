package local.pkovalev.adventofcode.aoc2023.utils;

public class Utils {
    public static long greatestCommonDivisor(long first, long second) {
        long a = Math.max(first, second);
        long b= Math.min(first, second);

        while(b >0) {
            long tmp = b;
            b = a % b;
            a = tmp;
        }
        return a;
    }

    public static long lesserCommonMultiple(long first, long second) {
        return Math.abs(first * (second / greatestCommonDivisor(first, second)));
    }
}
