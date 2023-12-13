package a01b.e2;

import java.util.Set;

public interface Controller {
    
    public void setGridSize(final int size);

    public void select(final Coord coord);

    public Set<Coord> lastEnabledCoords();

    public Set<Coord> lastDisabledCoords();

    public boolean isOver();

}
