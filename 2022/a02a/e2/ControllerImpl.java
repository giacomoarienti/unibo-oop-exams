package a02a.e2;

import java.time.Instant;
import java.util.*;

public class ControllerImpl implements Controller {

    private final List<Coord> numbers;
    private final Random random;
    private final int gridSize;
    private Direction direction;

    public ControllerImpl(final int size) {
        this.gridSize = size;
        numbers = new ArrayList<>();
        random = new Random(Instant.now().getEpochSecond());
    }

    @Override
    public Optional<Pair<Coord, Integer>> newNumber() {
        /* generate a random position if counter is 0 */
        if(numbers.isEmpty()) {
            final var coord = this.getRandomCoord();
            this.direction = Direction.NORTH;
            return this.saveCoord(Optional.of(coord));
        }
        final var coord = this.getNextCoord();
        return this.saveCoord(coord);
    }

    private Optional<Pair<Coord, Integer>> saveCoord(final Optional<Coord> coord) {
        if(coord.isEmpty()) {
            return Optional.empty();
        }
        final var realCoord = coord.get();
        /* save coord to set */
        final int counter = numbers.size();
        numbers.add(realCoord);
        /* return pair coord and counter */
        return Optional.of(new Pair<>(realCoord, counter));
    }

    private Optional<Coord> getNextCoord() {
        final Set<Direction> testedDirections = new HashSet<>();
        return getNextCoordRecursive(testedDirections);
    }

    private Optional<Coord> getNextCoordRecursive(final Set<Direction> testedDirections) {
        final var lastCoord = getLastCoord();
        final Coord nextCoord = switch(direction) {
            case NORTH -> new Coord(lastCoord.row() - 1, lastCoord.col());
            case SOUTH -> new Coord(lastCoord.row() + 1, lastCoord.col());
            case EST -> new Coord(lastCoord.row(), lastCoord.col() + 1);
            case WEST -> new Coord(lastCoord.row(), lastCoord.col() - 1);
            default -> throw new IllegalArgumentException("Unexpected value: " + direction);
        };
        testedDirections.add(direction);
        /* if new coord is valid, return it */
        if(isValidCoord(nextCoord)) {
            return Optional.of(nextCoord);
        }
        /* if all directions have been tested */
        if(testedDirections.size() == getDirectionList().size()) {
            return Optional.empty();
        }
        setNextDirection(testedDirections);
        return getNextCoordRecursive(testedDirections);
    }

    private void setNextDirection(final Set<Direction> testedDirections) {
        final var directions = getDirectionList();
        Direction nextDirection = direction;
        while(testedDirections.contains(nextDirection)) {
            nextDirection = directions.get(random.nextInt(directions.size()));
        }
        direction = nextDirection;
    }

    /* if coord is in bounds and was not extracted yet */
    private boolean isValidCoord(final Coord coord) {
        return coord.col() < gridSize && coord.col() >= 0
            && coord.row() >= 0 && coord.row() < gridSize
            && !numbers.contains(coord);
    }

    /* return a random coord in grid */
    private Coord getRandomCoord() {
        final int randomRow = random.nextInt(gridSize);
        final int randomCol = random.nextInt(gridSize);
        return new Coord(randomRow, randomCol);
    }

    private List<Direction> getDirectionList() {
        return List.of(Direction.values());
    }

    private Coord getLastCoord() {
        return numbers.get(numbers.size() - 1);
    }

    private enum Direction {
        NORTH,
        SOUTH,
        EST,
        WEST
    };

}
