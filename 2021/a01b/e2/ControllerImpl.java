package a01b.e2;

import java.util.*;

public class ControllerImpl implements Controller {

    private static final int MAX_POINTS = 3;

    private final List<Coord> points = new ArrayList<>();
    private final int size;

    public ControllerImpl(final int size) {
        this.size = size;
    }

    @Override
    public Optional<Integer> addPoint(final Coord coord) {
        if(isOver()) {
            throw new IllegalStateException("game is over");
        }
        /* check if coord is valid */
        if(!isValidCoord(coord)) {
            throw new IllegalArgumentException("invalid coord");
        }
        /* add point according to state */
        switch (points.size()) {
            case 0 -> {
                points.add(coord);
                return Optional.of(1);
            }
            case 1 -> {
                final var first = points.get(0);
                if(coord.x() == first.x() || coord.y() == first.y()) {
                    points.add(coord);
                    return Optional.of(2);
                }
            }
            case 2 -> {
                final var first = points.get(0);
                final var second = points.get(1);
                if((first.x() == second.x() && first.y() == coord.y())
                   || (first.y() == second.y() && first.x() == coord.x())) {
                    points.add(coord);
                    return Optional.of(3);
                }
            }
            default -> throw new IllegalStateException("invalid state");
        }
        return Optional.empty();
    }

    @Override
    public boolean isOver() {
        return points.size() == MAX_POINTS;
    }

    @Override
    public Set<Coord> getAngle() {
        if(!isOver()) {
            throw new IllegalStateException("not enough points");
        }
        final var first = points.get(0);
        final var second = points.get(1);
        final var third = points.get(2);
        /* join coords in between angle */
        final Set<Coord> coords = new HashSet<>();
        coords.addAll(coordsInBetween(first, second));
        coords.addAll(coordsInBetween(first, third));
        return coords;
    }

    private boolean isValidCoord(final Coord coord) {
        return coord.x() >= 0 && coord.y() >= 0 
            && coord.x() < size && coord.y() < size;
    }

    private Set<Coord> coordsInBetween(final Coord coord1, final Coord coord2) {
        final Set<Coord> coords = new HashSet<>();
        final int start;
        final int stop;
        if(coord1.x() == coord2.x()) {
            if(coord1.y() > coord2.y()) {
                start = coord2.y();
                stop = coord1.y();
            } else {
                start = coord1.y();
                stop = coord2.y();
            }
            for(int y = start+1; y < stop; y++) {
                coords.add(new Coord(coord1.x(), y));
            }
        } else {
            if(coord1.x() > coord2.x()) {
                start = coord2.x();
                stop = coord1.x();
            } else {
                start = coord1.x();
                stop = coord2.x();
            }
            for(int x = start+1; x < stop; x++) {
                coords.add(new Coord(x, coord1.y()));
            }
        }
        return coords;
    }

}
