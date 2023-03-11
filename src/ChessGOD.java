import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Moves.Move;
import Models.Moves.PieceMover;
import Models.PieceModels.Piece;
import Models.Player.Player;

import java.util.List;
import java.util.Scanner;

public class ChessGOD {

    public static void main(String[] args) {
        Boolean whiteTurn = true;

        Board myBoard = new Board();
        myBoard.fillBoard();

        InputProcessor myInputProcessor = new InputProcessor();

        Scanner myScanner = new Scanner(System.in);
        while(true){
            if(whiteTurn){
            }
            else{
            }

            System.out.print("Awaiting input (x,y-x,y) : ");
            String input = myScanner.nextLine();
            int[][] move = myInputProcessor.processInput(input);

            BoardPosition start = myBoard.boardState[move[0][1]][move[0][0]];
            BoardPosition end = myBoard.boardState[move[1][1]][move[1][0]];

            movePiece(myBoard, start, end);

            whiteTurn = !whiteTurn;
            myBoard.printBoard();
        }
    }


    public static void movePiece(Board myBoard, BoardPosition startPosition, BoardPosition endPosition) {
        if(startPosition.piece != null){
            List<Move> validMoves = startPosition.piece.canMove(startPosition, myBoard);

            Move myMove = null;

            for (Move move : validMoves) {
                if (move.moveTriggerPosition == endPosition) {
                    System.out.println("CHECKED");
                    myMove = move;
                    break;
                }
            }

            if (myMove != null) {
                PieceMover.movePiece(myBoard, myMove);
            } else {
                System.out.println("Invalid move");
            }
        }
        else{
            System.out.println("No piece on position");
        }
    }
}