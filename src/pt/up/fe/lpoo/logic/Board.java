/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic;

import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.Wall;
import pt.up.fe.lpoo.logic.piece.itemizable.Blank;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;
import pt.up.fe.lpoo.logic.piece.itemizable.Eagle;
import pt.up.fe.lpoo.logic.piece.itemizable.Hero;

import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;

public class Board implements Serializable {
    public enum Type {WALL, HERO, SWORD, DRAGON, MIXED_SD, EXIT, BLANK, LOCKED_WALL, EAGLE};

    public enum Direction {UP, LEFT, DOWN, RIGHT};
    public enum State {RUNNING, LOST, WON};

    private enum DragonFightResult {NOT_FOUND, ALREADY_DEFEATED, LOST, WON};
    private enum DragonSearchResult {NOT_FOUND, FOUND, ALREADY_DEFEATED};

    private State _gameState = State.RUNNING;

    private ArrayList<Piece> _boardPieces = new ArrayList<Piece>();

    private int width = 10;
    private int height = 10;
    
    private Eagle _eagle = null;

    public void setEagle(Eagle E1) {
        _eagle = E1;
    }

    public void setGameState(State st) {
        _gameState = st;
    }

    public Eagle getEagle() {
        return _eagle;
    }

    public Piece getPiece(Coordinate crd) throws Exception {
        if (_eagle != null)
            if (_eagle.getCoordinate().equals(crd))
                return _eagle;

        for (Piece pc : _boardPieces)
            if (pc.getCoordinate().equals(crd))
                return pc;

        throw new Exception("404: Piece Not Found");
    }

    public ArrayList<Piece> getPiecesWithType(Type type) throws Exception {
        ArrayList<Piece> pcs = new ArrayList<Piece>();

        for (Piece pc : _boardPieces) {
            switch (type) {
                case WALL:

                    if (pc instanceof Wall)
                        pcs.add(pc);

                    break;

                case HERO:

                    if (pc instanceof Hero)
                        pcs.add(pc);

                    break;

                case SWORD:

                    if (pc instanceof Blank && ((Blank) pc).getHasItem())
                        pcs.add(pc);

                    break;

                case DRAGON:

                    if (pc instanceof Dragon)
                        pcs.add(pc);

                    break;

                case EXIT:

                    if (pc instanceof Blank && ((Blank) pc).getIsExit())
                        pcs.add(pc);

                    break;

                case BLANK:

                    if (pc instanceof Blank)
                        pcs.add(pc);

                    break;

                default:

                    throw new Exception("Undefined or Private type requested.");
            }
        }

        return pcs;
    }

    public void addPiece(Piece piece) throws Exception {
        try {
            getPiece(piece.getCoordinate());
        } catch (Exception exc) {
            _boardPieces.add(piece);

            return;
        }

        throw new Exception("Piece already exists at coordinate.");
    }

    public void removePiece(Piece piece) throws Exception {
        _boardPieces.remove(piece);
    }

    public void setWidth(int w) {
        width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public State getGameState() {
        return _gameState;
    }

    public void setBoardPieces(ArrayList<Piece> board) {
        _boardPieces = board;
    }

    public ArrayList<Piece> getBoardPieces() {
        return _boardPieces;
    }

    private ArrayList<Direction> _getMovePossibilities(Piece pc) throws Exception {
        ArrayList<Direction> pb = new ArrayList<Direction>();

        try {
            getPiece(new Coordinate(pc.getCoordinate().x - 1, pc.getCoordinate().y));

            pb.add(Direction.LEFT);
        } catch (Exception e) {

        }

        try {
            getPiece(new Coordinate(pc.getCoordinate().x + 1, pc.getCoordinate().y));

            pb.add(Direction.RIGHT);
        } catch (Exception e) {

        }

        try {
            getPiece(new Coordinate(pc.getCoordinate().x, pc.getCoordinate().y - 1));

            pb.add(Direction.UP);
        } catch (Exception e) {

        }

        try {
            getPiece(new Coordinate(pc.getCoordinate().x, pc.getCoordinate().y + 1));

            pb.add(Direction.DOWN);
        } catch (Exception e) {

        }

        return pb;
    }

    public void moveDragon() throws Exception {
        ArrayList<Piece> dragons = getPiecesWithType(Type.DRAGON);

        for (Piece drag : dragons) {
            try {
                ArrayList<Direction> dir = _getMovePossibilities(drag);

                Random rand = new Random();

                int randVal = rand.nextInt(dir.size() + 1);

                if (dir.size() == randVal)
                    continue;

                drag.move(dir.get(randVal));
            } catch (Exception exc) {

            }
        }
    }

    private DragonSearchResult _isNearDragon() throws Exception {
        try {
            getPiecesWithType(Type.DRAGON).get(0);
        } catch (Exception exc) {
            return DragonSearchResult.ALREADY_DEFEATED;
        }

        ArrayList<Piece> dragons = getPiecesWithType(Type.DRAGON);

        Hero hero = (Hero) getPiecesWithType(Type.HERO).get(0);

        for (Piece d : dragons) {
            Dragon drag = (Dragon) d;

            if (hero.getCoordinate().x == drag.getCoordinate().x)
                if (hero.getCoordinate().y == drag.getCoordinate().y || hero.getCoordinate().y - 1 == drag.getCoordinate().y || hero.getCoordinate().y + 1 == drag.getCoordinate().y)
                    return DragonSearchResult.FOUND;

            if (hero.getCoordinate().y == drag.getCoordinate().y)
                if (hero.getCoordinate().x == drag.getCoordinate().x || hero.getCoordinate().x - 1 == drag.getCoordinate().x || hero.getCoordinate().x + 1 == drag.getCoordinate().x)
                    return DragonSearchResult.FOUND;
        }

        return DragonSearchResult.NOT_FOUND;
    }

    private Dragon _dragonNearHero() throws Exception {
        try {
            getPiecesWithType(Type.DRAGON).get(0);
        } catch (Exception exc) {
            throw exc;
        }

        ArrayList<Piece> dragons = getPiecesWithType(Type.DRAGON);

        Hero hero = (Hero) getPiecesWithType(Type.HERO).get(0);

        for (Piece d : dragons) {
            Dragon drag = (Dragon) d;

            if (hero.getCoordinate().x == drag.getCoordinate().x)
                if (hero.getCoordinate().y == drag.getCoordinate().y || hero.getCoordinate().y - 1 == drag.getCoordinate().y || hero.getCoordinate().y + 1 == drag.getCoordinate().y)
                    return drag;

            if (hero.getCoordinate().y == drag.getCoordinate().y)
                if (hero.getCoordinate().x == drag.getCoordinate().x || hero.getCoordinate().x - 1 == drag.getCoordinate().x || hero.getCoordinate().x + 1 == drag.getCoordinate().x)
                    return drag;
        }

        return null;
    }

    public DragonFightResult checkDragon() throws Exception {
        Dragon drag;

        try {
            drag = _dragonNearHero();

            if (drag == null)
                return DragonFightResult.NOT_FOUND;
        } catch (Exception exc) {
            return DragonFightResult.ALREADY_DEFEATED;
        }

        Hero hero = (Hero) getPiecesWithType(Type.HERO).get(0);

        if (hero.getHasItem()) {
            Blank bl = new Blank(drag.getCoordinate());

            _boardPieces.add(bl);

            removePiece(drag);

            return DragonFightResult.WON;
        }

        _boardPieces.add(new Blank(hero.getCoordinate()));

        removePiece(hero);

        _gameState = State.LOST;

        return DragonFightResult.LOST;
    }

    public void recheckGameState() throws Exception {
        if (_gameState == State.LOST || _gameState == State.WON)
            return;

        ArrayList<Piece> blanks = getPiecesWithType(Type.BLANK);

        if (getPiecesWithType(Type.HERO).size() == 0) {
            _gameState = State.LOST;

            return;
        }

        for (Piece bl : blanks)
            if (((Blank) bl).getIsExit()) {
                _gameState = State.RUNNING;

                return;
            }

        _gameState = State.WON;
    }
}
