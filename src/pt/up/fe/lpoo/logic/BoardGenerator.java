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
import pt.up.fe.lpoo.logic.piece.itemizable.Hero;

import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;

public class BoardGenerator {
    private int _width = 0;     //  x size
    private int _height = 0;    //  y size
    private int _dragons = 0;   // nr of dragons

    private Dragon.Behavior _behavior = Dragon.Behavior.STOP;

    private Board.Type boardRep[][];

    private Board.Type defaultBoardRep[][] = {
            {Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.HERO, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.DRAGON, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.EXIT},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.SWORD, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL}
    };

    private Board.Type defaultTestThreeDragonsRep[][] = {
            {Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.HERO, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.DRAGON, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.DRAGON, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.BLANK, Board.Type.DRAGON, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.EXIT},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.SWORD, Board.Type.WALL, Board.Type.WALL, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.BLANK, Board.Type.WALL},
            {Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL}
    };

    private BoardGenerator() {
        //  Calling this method directly is unsupported.
    }

    public BoardGenerator(int w, int h, int d) throws Exception {
        if (w < 3 || h < 3)
            throw new Exception("Both dimensions need to be >= 3.");

        boardRep = new Board.Type[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                boardRep[i][j] = ((i == 0 || i == (h - 1)) ? Board.Type.LOCKED_WALL :
                        ((j == 0 || j == (w - 1)) ? Board.Type.LOCKED_WALL :
                                Board.Type.WALL));
            }
        }

        _width = w;
        _height = h;
        _dragons = d;
    }

    public void setDragonBehavior(Dragon.Behavior bhv) {
        _behavior = bhv;
    }

    public void _generateBoardWithInternalFormat() throws Exception {
        Coordinate stp;

        try {
            stp = _startingPoint();
        } catch (Exception exc) {
            throw exc;
        }

        boardRep[stp.y][stp.x] = Board.Type.EXIT;

        Coordinate position = new Coordinate(stp.x, stp.y);

        Stack<Coordinate> crdHistory = new Stack<Coordinate>();

        while (true) {
            Coordinate[] moves = _possibleMoves(position);

            Random rand = new Random();

            if (moves.length == 0) {
                if (crdHistory.size() == 0)
                    break;

                position = crdHistory.pop();

                continue;
            }

            int val = rand.nextInt(moves.length);

            try {
                boardRep[moves[val].y][moves[val].x] = Board.Type.BLANK;
            } catch (Exception exc) {
                break;
            }

            position = moves[val];

            crdHistory.push(position);
        }

        for (int i = 0; i < _height; i++)
            for (int j = 0; j < _width; j++)
                if (boardRep[i][j] == Board.Type.LOCKED_WALL)
                    boardRep[i][j] = Board.Type.WALL;

        if (!_validateBoard())
            _generateBoardWithInternalFormat();

        _fillWithCharacters();
    }

    public ArrayList<Piece> generateBoard() throws Exception {
        ArrayList<Piece> retVec = new ArrayList<Piece>();

        if (boardRep != defaultBoardRep && boardRep != defaultTestThreeDragonsRep)
            _generateBoardWithInternalFormat();

        for (int i = 0; i < _height; i++)
            for (int j = 0; j < _width; j++) {
                switch (boardRep[i][j]) {
                    case WALL:

                        retVec.add(new Wall(j, i));

                        break;

                    case HERO:

                        retVec.add(new Hero(j, i));

                        break;

                    case SWORD: {
                        Blank bl = new Blank(j, i);

                        bl.setHasItem(true);

                        retVec.add(bl);

                        break;
                    }

                    case DRAGON:

                        Dragon drag = new Dragon(j, i);

                        drag.setBehavior(_behavior);

                        retVec.add(drag);

                        break;

                    case MIXED_SD:

                        break;

                    case EXIT: {
                        Blank bl = new Blank(j, i);

                        bl.setIsExit(true);

                        retVec.add(bl);

                        break;
                    }

                    case BLANK:

                        retVec.add(new Blank(j, i));

                        break;

                    default:

                        throw new Exception("Umm. Did you just invent a new piece, mate?");
                }
            }

        return retVec;
    }

    public ArrayList<Piece> getDefaultBoard() throws Exception {
        boardRep = defaultBoardRep;

        return generateBoard();
    }

    public ArrayList<Piece> getDragonTestBoard() throws Exception {
        boardRep = defaultTestThreeDragonsRep;

        return generateBoard();
    }

    private void _fillWithCharacters() {
        Coordinate[] whiteSpaces = _getWhitespaces();

        Random rand = new Random();

        int toGenerate = 2 + _dragons;

        ArrayList<Coordinate> usedCoordinates = new ArrayList<Coordinate>();

        for (int i = 0; i < toGenerate; i++) {
            if (i == 0) {
                Coordinate crd = whiteSpaces[rand.nextInt(whiteSpaces.length)];

                usedCoordinates.add(crd);

                boardRep[crd.y][crd.x] = Board.Type.HERO;

                continue;
            } else if (i == 1) {
                Coordinate crd = whiteSpaces[rand.nextInt(whiteSpaces.length)];

                while (crd.y == usedCoordinates.get(0).y && crd.x == usedCoordinates.get(0).x)
                    crd = whiteSpaces[rand.nextInt(whiteSpaces.length)];

                usedCoordinates.add(crd);

                boardRep[crd.y][crd.x] = Board.Type.SWORD;
            } else {
                Coordinate crd = whiteSpaces[rand.nextInt(whiteSpaces.length)];

                for (int j = 0; j < i; j++)
                    if (crd.y == usedCoordinates.get(j).y && crd.x == usedCoordinates.get(j).x) {
                        crd = whiteSpaces[rand.nextInt(whiteSpaces.length)];

                        j = 0;
                    }

                usedCoordinates.add(crd);

                boardRep[crd.y][crd.x] = Board.Type.DRAGON;
            }
        }
    }

    private Boolean _validateBoard() {
        for (int i = 0; i < _height; i++)
            for (int j = 0; j < _width; j++)
                try {
                    if (boardRep[i][j] == Board.Type.BLANK &&
                            boardRep[i + 1][j] == Board.Type.BLANK &&
                            boardRep[i][j + 1] == Board.Type.BLANK &&
                            boardRep[i + 1][j + 1] == Board.Type.BLANK)
                        return false;
                } catch (Exception exc) {

                }

        return true;
    }

    private Coordinate[] _getWhitespaces() {
        Coordinate coords[] = new Coordinate[_width * _height];

        int currIndex = 0;

        for (int i = 0; i < _height; i++)
            for (int j = 0; j < _width; j++)
                if (boardRep[i][j] == Board.Type.BLANK)
                    coords[currIndex++] = new Coordinate(j, i);

        Coordinate crdRet[] = new Coordinate[currIndex];

        for (int i = 0; i < currIndex; i++)
            crdRet[i] = coords[i];

        return crdRet;
    }

    private Coordinate[] _possibleMoves(Coordinate pos) {
        /*
         *  Th'arr be monst'arrs below, lad!
         *  Don't dive too deep without the help of your crew, capt'n!
         */

        Coordinate[] crds = new Coordinate[4];

        int ccIndex = 0;

        //  Move Up

        try {
            if (boardRep[pos.y - 1][pos.x] == Board.Type.WALL)
                if (!(boardRep[pos.y - 1][pos.x - 1] == Board.Type.BLANK ||
                        boardRep[pos.y - 2][pos.x - 1] == Board.Type.BLANK ||
                        boardRep[pos.y - 2][pos.x] == Board.Type.BLANK))
                    if (!(boardRep[pos.y - 2][pos.x + 1] == Board.Type.BLANK ||
                            boardRep[pos.y - 1][pos.x + 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x, pos.y - 1);
        } catch (Exception exc) {

        }

        //  Move Down

        try {
            if (boardRep[pos.y + 1][pos.x] == Board.Type.WALL)
                if (!(boardRep[pos.y + 1][pos.x - 1] == Board.Type.BLANK ||
                        boardRep[pos.y + 2][pos.x - 1] == Board.Type.BLANK ||
                        boardRep[pos.y + 2][pos.x] == Board.Type.BLANK))
                    if (!(boardRep[pos.y + 2][pos.x + 1] == Board.Type.BLANK ||
                            boardRep[pos.y + 1][pos.x + 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x, pos.y + 1);
        } catch (Exception exc) {

        }

        //  Move Right

        try {
            if (boardRep[pos.y][pos.x + 1] == Board.Type.WALL)
                if (!(boardRep[pos.y - 1][pos.x + 1] == Board.Type.BLANK ||
                        boardRep[pos.y - 1][pos.x + 2] == Board.Type.BLANK ||
                        boardRep[pos.y][pos.x + 2] == Board.Type.BLANK))
                    if (!(boardRep[pos.y + 1][pos.x + 2] == Board.Type.BLANK ||
                            boardRep[pos.y + 1][pos.x + 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x + 1, pos.y);
        } catch (Exception exc) {

        }

        //  Move Left

        try {
            if (boardRep[pos.y][pos.x - 1] == Board.Type.WALL)
                if (!(boardRep[pos.y - 1][pos.x - 1] == Board.Type.BLANK ||
                        boardRep[pos.y - 1][pos.x - 2] == Board.Type.BLANK ||
                        boardRep[pos.y][pos.x - 2] == Board.Type.BLANK))
                    if (!(boardRep[pos.y + 1][pos.x - 2] == Board.Type.BLANK ||
                            boardRep[pos.y + 1][pos.x - 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x - 1, pos.y);
        } catch (Exception exc) {

        }

        Coordinate[] crdRet = new Coordinate[ccIndex];

        for (int i = 0; i < ccIndex; crdRet[i] = crds[i++]) {

        }

        return crdRet;
    }

    private Coordinate _startingPoint() throws Exception {
        //  Choosing the Column...

        Random rand = new Random();

        switch (rand.nextInt(4)) {
            case 0:  // Top-Most Line

                return new Coordinate(rand.nextInt(_width - 2) + 1, 0);

            case 1: //  Right-Most Line

                return new Coordinate(9, rand.nextInt(_height - 2) + 1);

            case 2: //  Bottom-Most Line

                return new Coordinate(rand.nextInt(_width - 2) + 1, 9);

            case 3: //  Left-Most Line

                return new Coordinate(0, rand.nextInt(_height - 2) + 1);

            default:    //  ...

                break;
        }

        throw new Exception("Say What?!");
    }
}
