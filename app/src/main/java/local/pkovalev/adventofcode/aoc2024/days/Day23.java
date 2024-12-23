package local.pkovalev.adventofcode.aoc2024.days;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Slf4j
public class Day23 extends DayBase {

    Map<String, Set<String>> relations = new HashMap<>();

    @Override
    public void init() {
        parseData(data);
    }

    void parseData(List<String> data) {
        data.forEach( line -> {
           var items = line.split("-");
           if (relations.containsKey(items[0])) {
               relations.get(items[0]).add(items[1]);
           }
           else {
               var set = new HashSet<String>();
               set.add(items[1]);
               relations.put(items[0], set);
           }
            if (relations.containsKey(items[1])) {
                relations.get(items[1]).add(items[0]);
            }
            else {
                var set = new HashSet<String>();
                set.add(items[0]);
                relations.put(items[1], set);
            }
        });
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        relations.forEach( (key, val) -> {
            var connected = val.toArray();
            for (int i = 0; i < val.size() -1; i++ ) {
                for (int j = i+1; j < val.size(); j++) {
                    if (isConnected(key, (String)connected[i], (String)connected[j])) {
                        if (key.startsWith("t") || ((String) connected[i]).startsWith("t") || ((String) connected[j]).startsWith("t")) {
                            result.getAndIncrement();
                        }
                    }
                }
            }
        });
        return result.get()/3;
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        Set<String> longestSet = null;
        int longest = -1;
        for ( var item: relations.entrySet()) {
            var set = bestIntersection(item.getKey());
            if(set.size() > longest) {
                longest = set.size();
                longestSet = set;
            }
        }
        StringBuilder sb = new StringBuilder();
        longestSet.stream().sorted().forEach(str -> {
            sb.append(str);
            sb.append(',');
        });
        sb.deleteCharAt(sb.length()-1);
        log.info("Longest: {}", sb);
        return result.get();
    }

    boolean isConnected(String a, String b, String c) {
        return relations.get(a).contains(b) && relations.get(a).contains(c)
        && relations.get(b).contains(a) && relations.get(b).contains(c)
        && relations.get(c).contains(a) && relations.get(c).contains(b);
    }

    Set<String> bestIntersection(String a) {
        var matrix = buildMatrix(a);
        while (!trueMatrix(matrix)) {
            matrix = reduceMatrix(matrix);
        }
        return matrix.keySet();
    }

    boolean trueMatrix(Map<String, Map<String, Boolean>> matrix) {
        AtomicBoolean ret = new AtomicBoolean(true);
        matrix.values().forEach( m -> {
            m.values().stream().filter(Predicate.not(Boolean::booleanValue)).findAny().ifPresent((val) ->ret.set(false));
        });
        return ret.get();
    }

    Map<String, Map<String, Boolean>> reduceMatrix(Map<String, Map<String, Boolean>> matrix) {
        long min = matrix.size();
        String toDelete = null;
        for( var item : matrix.entrySet()) {
            long matches = item.getValue().values().stream().filter(Boolean::booleanValue).count();
            if(matches < min) {
                min = matches;
                toDelete = item.getKey();
            }
        }
        if(toDelete != null) {
            String finalToDelete = toDelete;
            matrix.forEach((k, v) -> {
                v.remove(finalToDelete);
            });
            matrix.remove(finalToDelete);
        }
       return matrix;
    }

    Map<String, Map<String, Boolean>> buildMatrix(String a) {
        Map<String, Map<String, Boolean>> matrix = new HashMap<>();
        matrix.put(a, new HashMap<>());
        matrix.get(a).put(a, true);
        relations.get(a).forEach(str -> {
            matrix.get(a).put(str, true);
            matrix.put(str, new HashMap<>());
            matrix.get(str).put(a, true);
            relations.get(a).forEach( str2 -> {
                matrix.get(str).put(str2, relations.get(str).contains(str2) || str.equals(str2));
            });
        });
        return matrix;
    }
}
