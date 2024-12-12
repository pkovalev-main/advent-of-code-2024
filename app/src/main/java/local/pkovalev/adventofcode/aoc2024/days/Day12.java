package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.MatrixPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Day12 extends DayBase {

    int mapRows = 0;
    int mapCols = 0;
    Plot[][] map = null;

    int index  = 1;

    static class Plot {
        public char label;
        public int groupId;
        public int perimeter = 0;

        Plot(char c, int i) {
            this.label = c;
            this.groupId = i;
        }
    }

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        AtomicLong result = new AtomicLong(0L);
        HashMap<Integer, Integer> areas = new HashMap<>();
        for (int row = 0; row < mapRows; row++) {
            for (int col = 0; col < mapCols; col++) {
                fillPerimeters(new MatrixPoint(row, col));
                var area = area(new MatrixPoint(row, col), index);
                if(area > 0) {
                    areas.put(index, area);
                    index++;
                }
            }
        }
        result.set(areas.keySet().stream().mapToInt(x -> {
            var perimeter = perimeterForGroupId(x);
            return perimeter * areas.get(x);
        }).sum());

        return result.get();
    }

    @Override
    public Long solvePartTwo() {
        init();
        AtomicLong result = new AtomicLong(0L);
        HashMap<Integer, Integer> areas = new HashMap<>();
        for (int row = 0; row < mapRows; row++) {
            for (int col = 0; col < mapCols; col++) {
                fillPerimeters(new MatrixPoint(row, col));
                var area = area(new MatrixPoint(row, col), index);
                if(area > 0) {
                    areas.put(index, area);
                    index++;
                }
            }
        }
        result.set(areas.keySet().stream().mapToInt(x -> {
            var sides = calcHSides(x) + calcVSides(x);
            return sides * areas.get(x);
        }).sum());

        return result.get();
    }

    void parseData(List<String> data) {
        mapRows = data.size();
        mapCols = data.get(0).length();
        map = new Plot[mapRows][mapCols];
        AtomicInteger currentRow = new AtomicInteger(0);
        data.forEach(row -> {
            for (int i = 0 ; i < row.length(); i++) {
                map[currentRow.get()][i] = new Plot(row.charAt(i), 0);
            }
            currentRow.getAndIncrement();
        });
    }

    int perimeterForGroupId(int groupId) {
        int ret = 0;
        for (int row = 0; row < mapRows; row++) {
            for (int col = 0; col < mapCols; col++) {
                if(plotAtPoint(new MatrixPoint(row, col)).groupId == groupId) {
                    ret += plotAtPoint(new MatrixPoint(row, col)).perimeter;
                }
            }
        }
        return ret;
    }

    void fillPerimeters(MatrixPoint point) {
        List<MatrixPoint> surrounding = List.of(point.newAbove(), point.newBelow(), point.newLeft(), point.newRight());
        surrounding.forEach( x -> {
            if (x.inside(mapRows, mapCols)) {
                if (plotAtPoint(x).label == plotAtPoint(point).label) {
                    return;
                }
            }
            plotAtPoint(point).perimeter += 1;
        });
    }

    int area(MatrixPoint point, int groupId) {
        if (plotAtPoint(point).groupId !=  0) {
            return 0;
        }
        char label = plotAtPoint(point).label;
        AtomicInteger area = new AtomicInteger(1);
        plotAtPoint(point).groupId = groupId;
        List<MatrixPoint> surrounding = List.of(point.newAbove(), point.newBelow(), point.newLeft(), point.newRight());
        surrounding.forEach( x -> {
            if (x.inside(mapRows, mapCols) && plotAtPoint(x).label == label) {
                area.addAndGet(area(x, groupId));
            }
        });
        return area.get();
    }

    int calcHSides(int groupId) {
        boolean topSide = false;
        boolean bottomSide = false;
        int sides = 0;
        for (int row = 0; row < mapRows; row++) {
            topSide = false;
            bottomSide = false;
            for (int col = 0; col < mapCols; col++) {
                MatrixPoint p = new MatrixPoint(row, col);
                var plot = plotAtPoint(p);
                if (plot.groupId == groupId) {
                    var top = p.newAbove();
                    if(top.inside(mapRows,mapCols) && plotAtPoint(top).groupId == groupId) {
                        topSide = false;
                    }
                    else {
                        if(!topSide) {
                            sides++;
                            topSide = true;
                        }
                    }
                    var bottom = p.newBelow();
                    if(bottom.inside(mapRows,mapCols) && plotAtPoint(bottom).groupId == groupId) {
                        bottomSide = false;
                    }
                    else {
                        if(!bottomSide) {
                            sides++;
                            bottomSide = true;
                        }
                    }
                }
                else {
                    topSide = false;
                    bottomSide = false;
                }
            }
        }
        return sides;
    }
    int calcVSides(int groupId) {
       boolean leftSide = false;
       boolean rightSide = false;
       int sides = 0;
       for (int col = 0; col < mapCols; col++) {
           leftSide = false;
           rightSide = false;
           for (int row = 0; row < mapRows; row++) {
               MatrixPoint p = new MatrixPoint(row, col);
               var plot = plotAtPoint(p);
               if (plot.groupId == groupId) {
                   var top = p.newLeft();
                   if(top.inside(mapRows,mapCols) && plotAtPoint(top).groupId == groupId) {
                       leftSide = false;
                   }
                   else {
                       if(!leftSide) {
                           sides++;
                           leftSide = true;
                       }
                   }
                   var bottom = p.newRight();
                   if(bottom.inside(mapRows,mapCols) && plotAtPoint(bottom).groupId == groupId) {
                       rightSide = false;
                   }
                   else {
                       if(!rightSide) {
                           sides++;
                           rightSide = true;
                       }
                   }
               }
               else {
                   rightSide = false;
                   leftSide = false;
               }
           }
       }
       return sides;
    }

    Plot plotAtPoint(MatrixPoint point) {
        return map[point.getRow()][point.getCol()];
    }
}
