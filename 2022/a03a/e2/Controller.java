package a03a.e2;

public interface Controller {
 
    enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    enum GameStatus {
        PLAYING("playing"),
        PLAYER_WINS("umano vince"),
        COMPUTER_WINS("computer vince");

        private final String status;

        private GameStatus(final String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public boolean equals(GameStatus g) {
            return g.getStatus().equals(status);
        }
    }
    
    enum Turn {
        PLAYER,
        COMPUTER
    }

    void resetGame();

    Coord getPlayerPosition();

    Coord getComputerPosition();

    /**
     * If possibile move the player to pos
     * @param pos the new player position
     * @return true if valid pos
     */
    boolean movePlayer(Coord pos);

    GameStatus getGameStatus();

    boolean isOver();

}
