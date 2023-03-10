import Models.Board.Board;
import Models.Player.Player;

public class ChessGOD {
    public static void main(String[] args) {

        Player white = new Player(true, "AY");
        Player black = new Player(false, "AY2");

        Board myBoard = new Board(white, black);


    }
}