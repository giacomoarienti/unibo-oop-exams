package a03a.e2;

import java.time.Instant;
import java.util.*;

public class ControllerImpl implements Controller {

    private final int size;
    private final Random rand;
    
    private Coord playerPos;
    private Coord computerPos; 
    private GameStatus status;
    private Turn turn;

    public ControllerImpl(final int size) {
        this.size = size;
        rand = new Random(Instant.now().getNano());
    }

    @Override
    public void resetGame() {
        this.turn = Turn.PLAYER;
        this.status = GameStatus.PLAYING;
        this.computerPos = randomPos();
        this.playerPos = randomPos();
        /* make sure no overlapping */
        while(playerPos.equals(computerPos)) {
            playerPos = randomPos();
        }
    }

    @Override
    public Coord getPlayerPosition() {
        return playerPos;
    }

    @Override
    public Coord getComputerPosition() {
        return computerPos;
    }

    @Override
    public boolean movePlayer(final Coord pos) {
        if(!isValidMove(playerPos, pos)) {
            return false;
        }
        playerPos = pos;
        updateGameStatus();
        if(!isOver()) {
            moveComputer();
        }
        return true;
    }

    @Override
    public GameStatus getGameStatus() {
        return status;
    }

    @Override
    public boolean isOver() {
        return !status.equals(GameStatus.PLAYING);
    }

    private void moveComputer() {
        /* if can eat player */
        if(canComputerEatPlayer()) {
            computerPos = playerPos;
        } else {
            computerPos = getRandomComputerPos();
        }
        updateGameStatus();
    }

    private boolean isValidCoord(final Coord coord) {
        return coord.X() < size && coord.X() >= 0 &&
            coord.Y() < size && coord.Y() >= 0;
    } 

    private boolean isValidMove(final Coord prevPos, final Coord newCord) {
        if(!isValidCoord(newCord) || !isValidCoord(prevPos)) {
            return false;
        }
        if(prevPos.equals(newCord)) {
            return false;
        }
        return prevPos.X() == newCord.X() || prevPos.Y() == newCord.Y();
    }

    private Coord randomPos() {
        return new Coord(
            rand.nextInt(size),
            rand.nextInt(size)
        );
    }

    private boolean checkCollision() {
        return playerPos.equals(computerPos);
    }

    private boolean canComputerEatPlayer() {
        return computerPos.X() == playerPos.X() || computerPos.Y() == playerPos.Y();
    }

    private void updateGameStatus() {
        switch (turn) {
            case PLAYER -> {
                if(checkCollision()) {
                    status = GameStatus.PLAYER_WINS;
                }
                turn = Turn.COMPUTER;
            }

            case COMPUTER -> {
                if(checkCollision()) {
                    status = GameStatus.COMPUTER_WINS;
                }
                turn = Turn.PLAYER;
            }
        
            default -> {
                throw new IllegalStateException("Invalid turn");
            }
        }
    }

    private Coord getPosFromDir(final Coord pos, final Direction dir, final int length) {
        return switch (dir) {
            case NORTH -> new Coord(pos.X() - length, pos.Y());
            case SOUTH -> new Coord(pos.X() + length, pos.Y());
            case EAST -> new Coord(pos.X(), pos.Y() + length);
            case WEST -> new Coord(pos.X() - length, pos.Y() - length);

            default -> {
                throw new IllegalArgumentException("Invalid direction");
            }
        };
    }

    private Coord getRandomComputerPos() {
        Coord newPos = new Coord(-1, -1);
        while(!isValidMove(computerPos, newPos)) {
            final Direction[] directions = Direction.values();
            final Direction dir = Direction.values()[rand.nextInt(directions.length)];
            final int length = rand.nextInt(size-1) + 1;
            newPos = getPosFromDir(computerPos, dir, length);
        }
        return newPos;
    }
}
