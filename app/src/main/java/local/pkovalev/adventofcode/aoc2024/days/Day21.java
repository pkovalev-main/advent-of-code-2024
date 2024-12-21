package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Matrix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Day21 extends DayBase {

    Matrix<Character> numericKb = new Matrix<>(0, 0);
    Matrix<Character> directionalKb = new Matrix<>(0, 0);

    List<String> codes;

    List<String> chunks = new ArrayList<>();
    HashMap<String, List<Integer>> chunksMapper = new HashMap<>();

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    static class CacheKey {
        String chunk;
        int level;
    }

    HashMap<CacheKey, Long> cache = new HashMap<>();

    @Override
    public void init() {
        parseData(data);

        numericKb.addRow(List.of('7', '8', '9'));
        numericKb.addRow(List.of('4', '5', '6'));
        numericKb.addRow(List.of('1', '2', '3'));
        numericKb.addRow(List.of(' ', '0', 'A'));

        directionalKb.addRow(List.of(' ', '^', 'A'));
        directionalKb.addRow(List.of('<', 'V', '>'));

        initChunks();
        initMapper();
    }

    void parseData(List<String> data) {
        codes = data;
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        codes.forEach(code -> {
            ArrayList<Character> sequence = new ArrayList<>();
            var start = 'A';
            for (var c : code.toCharArray()) {
                sequence.addAll(pushNextNumeric(start, c));
                start = c;
            }
            //log.info("{}({}) : {}", code, sequence.size(), sequence);
            start = 'A';
            ArrayList<List<Character>> arr = new ArrayList<>();
            for (var c : sequence) {
                var chunk = pushNextDirectional(start, c);
                arr.add(chunk);
                start = c;
            }
            AtomicLong len = new AtomicLong(0);
            arr.forEach( x -> {
                StringBuilder sb = new StringBuilder();
                x.forEach(sb::append);
                len.addAndGet(getLength(sb.toString(), 1));
            });
            log.info("{}({})", code, len.get());


            result.addAndGet(len.get() * Integer.parseInt(code.substring(0, code.length() - 1)));
        });
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        codes.forEach(code -> {
            ArrayList<Character> sequence = new ArrayList<>();
            var start = 'A';
            for (var c : code.toCharArray()) {
                sequence.addAll(pushNextNumeric(start, c));
                start = c;
            }
            //log.info("{}({}) : {}", code, sequence.size(), sequence);
            start = 'A';
            ArrayList<List<Character>> arr = new ArrayList<>();
            for (var c : sequence) {
                var chunk = pushNextDirectional(start, c);
                arr.add(chunk);
                start = c;
            }
            AtomicLong len = new AtomicLong(0);
            arr.forEach( x -> {
                StringBuilder sb = new StringBuilder();
                x.forEach(sb::append);
                len.addAndGet(getLength(sb.toString(), 25));
            });
            log.info("{}({})", code, len.get());


            result.addAndGet(len.get() * Integer.parseInt(code.substring(0, code.length() - 1)));
        });
        return result.get();
    }

    ArrayList<ArrayList<Character>> variants(List<Character> in) {
        if (in.size() == 1) {
            ArrayList<Character> seq = new ArrayList<>();
            seq.add(in.get(0));
            ArrayList<ArrayList<Character>> ret = new ArrayList<>();
            ret.add(seq);
            return ret;
        }
        ArrayList<ArrayList<Character>> ret = new ArrayList<>();
        var variants = variants(in.subList(1, in.size()));
        variants.forEach( v -> {
            ArrayList<ArrayList<Character>> seq = new ArrayList<>();
            for(int i = 0; i <= v.size(); i++) {
               var newList = new ArrayList<Character>(v);
               newList.add(i, in.get(0));
               seq.add(newList);
            }
            ret.addAll(seq);
        });
        return ret;
    }

    boolean validNumericPath(Character start, List<Character> seq) {
        var point = numericKb.findFirst(start).get();
        var denied = numericKb.findFirst(' ').get();
        for( var c : seq) {
            switch (c) {
                case '>' -> point.shift(0,1);
                case '<' -> point.shift(0,-1);
                case 'V' -> point.shift(1,0);
                case '^' -> point.shift(-1,0);
            }
            if (point.getRow() == denied.getRow() && point.getCol() == denied.getCol()) {
                return false;
            }
        }
        return true;
    }
    List<Character> pushNextNumeric(Character start, Character dest) {
        var sp = numericKb.findFirst(start).get();
        var dp = numericKb.findFirst(dest).get();
        var drow = dp.getRow() - sp.getRow();
        var dcol = dp.getCol() - sp.getCol();

        ArrayList<Character> ret = new ArrayList<>();
        for(int r = 0; r < Math.abs(drow); r++) {
            ret.add(drow > 0 ? 'V' : '^');
        }
        for(int c = 0; c < Math.abs(dcol); c++) {
            ret.add(dcol > 0 ? '>' : '<');
        }
        var variants = variants(ret).stream().distinct().filter(v -> validNumericPath(start, v)).toList();
        ret.clear();
        ret.addAll(getShortest(variants));
        ret.add('A');
        return ret;
    }

    List<Character> getShortest(List<ArrayList<Character>> arr) {
        int score = Integer.MAX_VALUE;
        List<Character> min = null;
        for (var seq : arr) {
            ArrayList<Character> directional1 = new ArrayList<>();
            ArrayList<Character> directional2 = new ArrayList<>();
            var newarr = new ArrayList<>(seq);
            newarr.add('A');
            var start = 'A';
            for (var c : newarr) {
                directional1.addAll(pushNextDirectional(start, c));
                start = c;
            }
            start = 'A';
            for (var c : directional1) {
                directional2.addAll(pushNextDirectional(start, c));
                start = c;
            }
            if(directional2.size() < score) {
                score = directional2.size();
                min = seq;
            }
        }
        return min;
    }

    List<Character> pushNextDirectional(Character start, Character dest) {
        var sp = directionalKb.findFirst(start).get();
        var dp = directionalKb.findFirst(dest).get();
        var drow = dp.getRow() - sp.getRow();
        var dcol = dp.getCol() - sp.getCol();

        ArrayList<Character> ret = new ArrayList<>();
        if(dcol > 0) {
            for(; dcol > 0; dcol--) {
                ret.add('>');
            }
        }
        if(drow < 0) {
            for(; drow < 0; drow++) {
                ret.add('^');
            }
        }
        if(drow > 0) {
            for(; drow > 0; drow--) {
                ret.add('V');
            }
        }
        if(dcol < 0) {
            for(; dcol < 0; dcol++) {
                ret.add('<');
            }
        }
        ret.add('A');
        return ret;
    }

    void initChunks() {
        var chars = List.of('>', '<', '^', 'V', 'A');
        for (var c1 : chars) {
            for (var c2 : chars) {
                var steps = pushNextDirectional(c1, c2);
                StringBuilder sb = new StringBuilder();
                steps.forEach(sb::append);
                chunks.add(sb.toString());
            }
        }
        chunks = chunks.stream().distinct().toList();
    }

    void initMapper() {
        chunks.forEach( chunk -> {
            char start = 'A';
            ArrayList<Integer> child = new ArrayList<>();
            for (var c: chunk.toCharArray()) {
                var newChunk = pushNextDirectional(start, c);
                start = c;
                StringBuilder sb = new StringBuilder();
                newChunk.forEach(sb::append);
                child.add(chunks.indexOf(sb.toString()));
            }
            chunksMapper.put(chunk, child);
        });
    }

    Long getLength(String chunkStr, int depth) {
        if (depth == 1) {
            return chunksMapper.get(chunkStr).stream().map(chunks::get).mapToLong(String::length).sum();
        }

        if (cache.containsKey(new CacheKey(chunkStr, depth))) {
            return cache.get(new CacheKey(chunkStr, depth));
        }

        var ret = chunksMapper.get(chunkStr).stream().map(chunks::get).mapToLong( x -> getLength(x, depth-1)).sum();
        cache.put(new CacheKey(chunkStr, depth), ret);
        return ret;
    }
}
