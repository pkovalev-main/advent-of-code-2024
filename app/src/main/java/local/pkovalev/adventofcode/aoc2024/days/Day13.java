package local.pkovalev.adventofcode.aoc2024.days;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

@Slf4j
public class Day13 extends DayBase {

    ArrayList<Game> games = new ArrayList<>();

    @Data
    static class Game {
        int ax;
        int ay;
        int bx;
        int by;
        long prizex;
        long prizey;
    }

    private final String pattern1 = "X\\+(\\d+), Y\\+(\\d+)";
    private final String pattern2 = "X=(\\d+), Y=(\\d+)";

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        result.addAndGet(games.stream().mapToLong(this::fastPrice).sum());
        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        AtomicLong result = new AtomicLong(0L);
        long shift = 10000000000000L;
        result.addAndGet(games.stream().peek(x -> {
            x.setPrizex(x.prizex + shift);
            x.setPrizey(x.prizey + shift);
        }).mapToLong(this::fastPrice).sum());
        return result.get();
    }

    void parseData(List<String> data) {
        Pattern p1 = Pattern.compile(pattern1);
        Pattern p2 = Pattern.compile(pattern1);
        Pattern prizeP = Pattern.compile(pattern2);
        for(int i = 0; i < data.size();) {
            var ma = p1.matcher(data.get(i));
            var mb = p2.matcher(data.get(i+1));
            var mp = prizeP.matcher(data.get(i+2));
            i+=4;
            ma.find();
            mb.find();
            mp.find();

            var game = new Game();
            game.setAx(Integer.parseInt(ma.group(1)));
            game.setAy(Integer.parseInt(ma.group(2)));
            game.setBx(Integer.parseInt(mb.group(1)));
            game.setBy(Integer.parseInt(mb.group(2)));
            game.setPrizex(Integer.parseInt(mp.group(1)));
            game.setPrizey(Integer.parseInt(mp.group(2)));

            games.add(game);
        }
    }

    long fastPrice(Game game) {
        var countb = (double) (game.prizey * game.ax - game.prizex * game.ay) / (double)(game.ax * game.by - game.ay * game.bx);
        var counta = (game.prizex - countb * (double)game.bx) / (double)game.ax;
        if (counta % 1 == 0 && countb % 1 == 0) {
            return ((long)counta) * 3 + (long) countb;
        }
        return 0;
    }

}
