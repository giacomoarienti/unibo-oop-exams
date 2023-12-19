package a01b.e2;

import java.util.Set;
import java.util.Optional;

public interface Controller {
    
    Optional<Integer> addPoint(Coord coord);

    boolean isOver();

    Set<Coord> getAngle();

}
