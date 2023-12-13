package a01b.e2;

import java.util.*;

public class ControllerImpl implements Controller {

    private final Set<Coord> activeCoords = new HashSet<>();
    private Set<Coord> lastEnabledCoords;
    private Set<Coord> lastDisabledCoords;
    private int gridSize;

    @Override
    public void setGridSize(final int size) {
        this.gridSize = size;
    }

    @Override
    public void select(final Coord coord) {
        if(Objects.isNull(gridSize)) {
            throw new IllegalStateException("Must setGridSize() first");
        }
        lastEnabledCoords = new HashSet<>();
        lastDisabledCoords = new HashSet<>();
        /* check which corners have to be enabled/disabled */
        final List<Coord> changeCoords = this.listCorners(coord);
        for (final Coord c: changeCoords) {
            if(activeCoords.contains(c)) {
                activeCoords.remove(c);
                lastDisabledCoords.add(c);
            } else {
                activeCoords.add(c);
                lastEnabledCoords.add(c);
            }
        }
    }

    @Override
    public Set<Coord> lastEnabledCoords() {
        if(Objects.isNull(lastEnabledCoords)) {
            throw new IllegalStateException("Must select() first");
        }
        return lastEnabledCoords;
    }

    @Override
    public Set<Coord> lastDisabledCoords() {
        if(Objects.isNull(lastDisabledCoords)) {
            throw new IllegalStateException("Must select() first");
        }
        return lastDisabledCoords;
    }

    @Override
    public boolean isOver() {
         if(Objects.isNull(lastDisabledCoords) || Objects.isNull(lastEnabledCoords)) {
            throw new IllegalStateException("Must select() first");
        }
        if(lastDisabledCoords.size() == 3 && lastEnabledCoords.size() == 1) {
            return true;
        }
        return false;
    }
    
    private Optional<Coord> topRight(final Coord coord) {
        if(coord.row() > 0 && coord.col() < (gridSize - 1)) {
            return Optional.of(new Coord(coord.row() - 1, coord.col() + 1));
        }
        return Optional.empty();
    }

    private Optional<Coord> topleft(final Coord coord) {
        if(coord.row() > 0 && coord.col() > 0) {
            return Optional.of(new Coord(coord.row() - 1, coord.col() - 1));
        }
        return Optional.empty();
    }

    private Optional<Coord> bottomLeft(final Coord coord) {
        if(coord.row() < (gridSize - 1) && coord.col() > 0) {
            return Optional.of(new Coord(coord.row() + 1, coord.col() - 1));
        }
        return Optional.empty();
    }

    private Optional<Coord> bottomRight(final Coord coord) {
        if(coord.row() < (gridSize - 1) && coord.col() < (gridSize - 1)) {
            return Optional.of(new Coord(coord.row() + 1, coord.col() + 1));
        }
        return Optional.empty();
    }

    private List<Coord> listCorners(final Coord coord) {
        return List.of(
            topRight(coord),
            topleft(coord),
            bottomLeft(coord),
            bottomRight(coord)
        ).stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
    }
}
