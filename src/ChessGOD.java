import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Player.Player;

import java.util.Scanner;

public class ChessGOD {
    public static void main(String[] args) {

        Player white = new Player(true, "AY");
        Player black = new Player(false, "AY2");
        Boolean whiteTurn = true;

        Board myBoard = new Board(white, black);
        InputProcessor myInputProcessor = new InputProcessor();

        Scanner myScanner = new Scanner(System.in);
        while(true){
            Player playerTurn;

            if(whiteTurn){
                playerTurn = white;
            }
            else{
                playerTurn = black;
            }

            System.out.print("Awaiting input (x,y-x,y) : ");
            String input = myScanner.nextLine();
            int[][] move = myInputProcessor.processInput(input);

            BoardPosition start = myBoard.boardState[move[0][1]][move[0][0]];
            BoardPosition end = myBoard.boardState[move[1][1]][move[1][0]];

            myBoard.movePiece(start, end);

            whiteTurn = !whiteTurn;

            myBoard.printBoard();
        }
    }
}