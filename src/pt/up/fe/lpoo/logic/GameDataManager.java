/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic;

import java.io.*;

public class GameDataManager {

    /**
     * Saves the current game to a file.
     *
     * @param board The board to save.
     * @param filePath The file destination.
     *
     * @return true on success, false otherwise.
     */

    public static boolean saveGameState(Board board, String filePath) {
        try {
            FileOutputStream os = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject(board);

            oos.close();
            os.close();
        } catch (IOException exc) {
            return false;
        }

        return true;
    }

    /**
     * Loads a saved game from the disk.
     *
     * @param filePath The path of the saved game.
     * @return The loaded game.
     *
     * @throws Exception Throws an exception if the game couldn't be loaded correctly.
     */

    public static Board loadGameState(String filePath) throws Exception {
        FileInputStream is = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(is);

        Board brd = (Board) ois.readObject();

        ois.close();
        is.close();

        return brd;
    }
}
