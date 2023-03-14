import Models.AI.Brain;
import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Board.PositionChecker;
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

        Brain myBrain = new Brain(5);
        Scanner myScanner = new Scanner(System.in);
        while(true){

            if(whiteTurn){
                System.out.println("WHITE");
                myBrain.recommendMove(true, myBoard.cloneBoard());
            }
            else{
                System.out.println("BLACK");
                myBrain.recommendMove(false, myBoard.cloneBoard());
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
                if(move != null){
                    if (move.moveTriggerPosition == endPosition) {
                        myMove = move;
                        break;
                    }
                }
            }

            if (myMove != null) {
                boolean isWhite = myMove.movePieceStartPosition.piece.isWhite;
                PieceMover.movePiece(myBoard, myMove);
                PositionChecker.checkForCheck(!isWhite,myBoard.cloneBoard(), true);
            } else {
                System.out.println("Invalid move");
            }
        }
        else{
            System.out.println("No piece on position");
        }
    }
}