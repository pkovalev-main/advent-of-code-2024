package local.pkovalev.adventofcode.aoc2024.days;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Day22 extends DayBase {

    long[] initialSecrets;

    @Data
    @EqualsAndHashCode
    static class CacheKey {
        int[] a = new int[4];

        CacheKey(int a, int b, int c, int d) {
            this.a[0] = a;
            this.a[1] = b;
            this.a[2] = c;
            this.a[3] = d;
        }
    }

    HashMap<CacheKey, MutableInt> cache = new HashMap<>();

    @Override
    public void init() {
        parseData(data);
    }

    void parseData(List<String> data) {
        initialSecrets = new long[data.size()];
        for (int i = 0; i < data.size(); i++)  {
            initialSecrets[i] = Integer.parseInt(data.get(i));
        }
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        for (long num : initialSecrets) {
            long newSecret = num;
            for(int i = 0; i < 2000; i++) {
                newSecret = getNextSecret(newSecret);
            }
            result.addAndGet(newSecret);
        }
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        for (long num : initialSecrets) {
            int[] seq = new int[2000];
            long newSecret = num;
            HashSet<CacheKey> processed = new HashSet<>();
            for(int i = 0; i < 2000; i++) {
                var a = getNextSecret(newSecret);
                seq[i] = (int)((a % 10) - (newSecret % 10));
                newSecret = a;

                if( i >= 3) {
                    var key = new CacheKey(seq[i-3], seq[i-2], seq[i-1], seq[i]);
                   if (!processed.contains(key)) {
                       processed.add(key);
                       if(cache.containsKey(key)) {
                           cache.get(key).add(newSecret % 10);
                       }
                       else {
                           cache.put(key, new MutableInt(newSecret % 10));
                       }
                   }
                }
            }
        }
        result.set(cache.values().stream().mapToInt(MutableInt::intValue).max().getAsInt());
        return result.get();
    }

    long getNextSecret(long secret) {
        long res = secret;
        res = ((res << 6) ^ res) % 16777216;
        res = ((res >> 5) ^ res) % 16777216;
        res = ((res << 11 ) ^ res) % 16777216;
        return res;
    }

}
