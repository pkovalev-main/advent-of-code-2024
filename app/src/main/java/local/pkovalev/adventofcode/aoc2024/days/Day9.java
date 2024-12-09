package local.pkovalev.adventofcode.aoc2024.days;

import local.pkovalev.adventofcode.aoc2024.utils.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class Day9 extends DayBase {

    @Data
    @AllArgsConstructor
    static
    class File {
        Long fileId;
        List<Long> positions;
    }

    ArrayList<File> files = new ArrayList<>();
    ArrayList<List<Long>> freeSpace = new ArrayList<>();

    @Override
    public void init() {
        parseData(data);
    }

    @Override
    public Long solvePartOne() {
        long result = 0L;
        var flatFiles = new ArrayList<Pair<Long,Long>>();
        files.forEach(file -> {
            file.positions.forEach( pos -> {
                flatFiles.add(new Pair<>(pos, file.getFileId()));
            });
        });
        AtomicInteger pos = new AtomicInteger(flatFiles.size() -1);
        ArrayList<Long> allSpaces = new ArrayList<>();
        freeSpace.forEach(allSpaces::addAll);
        allSpaces.forEach( slot -> {
            if(flatFiles.get(pos.get()).first() > slot) {
                flatFiles.get(pos.getAndDecrement()).setFirst(slot);
            }
        });

        result = flatFiles.stream().mapToLong(item -> item.first() * item.second()).sum();
        return result;
    }

    @Override
    public Long solvePartTwo() {
        long result = 0L;
        files.sort((a, b) -> -(a.fileId.compareTo(b.fileId)));

        files.forEach(this::moveFile);
        var flatFiles = new ArrayList<Pair<Long,Long>>();
        files.forEach(file -> {
            file.positions.forEach( pos -> {
                flatFiles.add(new Pair<>(pos, file.getFileId()));
            });
        });
        flatFiles.sort(Comparator.comparing(Pair::first));
        result = flatFiles.stream().mapToLong(item -> item.first() * item.second()).sum();
        return result;
    }

    void moveFile(File file) {
        var newLocation = freeSpace.stream().filter(x -> x.size() >= file.positions.size()).findFirst();
        if(newLocation.isPresent()) {
            var loc = newLocation.get();
            if(loc.get(0) >= file.positions.get(0)) {
                return;
            }
            var newPositions = loc.stream().limit(file.positions.size()).toList();
            file.setPositions(newPositions);
            freeSpace.set(freeSpace.indexOf(loc), loc.subList(file.positions.size(), loc.size()));
        }
    }
    void parseData(List<String> data) {
        char[] chars = data.get(0).toCharArray();
        boolean chunk = true;
        long chunkId = 0;
        long position = 0;
        for (char aChar : chars) {
            int digit = (int) aChar - 48;
            if(chunk) {
                ArrayList<Long> positions = new ArrayList<>(digit);
                for (int k = 0; k < digit; k++) {
                    positions.add(position);
                    position++;
                }
                files.add(new File(chunkId, positions));
            }
            else {
                ArrayList<Long> freeSpaces = new ArrayList<>(digit);
                for (int k = 0; k < digit; k++) {
                    freeSpaces.add(position);
                    position++;
                }
                freeSpace.add(freeSpaces);
                chunkId++;
            }
            chunk = !chunk;
        }
    }

}
