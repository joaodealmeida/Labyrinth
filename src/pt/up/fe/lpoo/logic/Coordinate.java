/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic;

import java.io.Serializable;

public class Coordinate implements Serializable {
    public int x;
    public int y;

    /**
     * Constructs a new coordinate with given X and Y parameters.
     *
     * @param  xCrd  The x-axis coordinate.
     * @param  yCrd  The y-axis coordinate.
     */

    public Coordinate(int xCrd, int yCrd) {
        x = xCrd;
        y = yCrd;
    }

    /**
     * Compares two Coordinate objects.
     *
     * @param  o  The object to compare to.
     * @return The comparison result.
     */

    public boolean equals(Coordinate o) {
        return (x == o.x && y == o.y);
    }
}
