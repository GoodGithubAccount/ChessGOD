package Models.Board;

import Models.Moves.Move;
import Models.Moves.PieceMover;
import Models.PieceModels.King;
import Models.PieceModels.Piece;

import java.util.ArrayList;
import java.util.List;

public class PositionChecker {

    // Checks for moves that would be illegal because you would give the opponent a check / mate
    public static boolean checkForCheck(boolean isWhite, Board myBoard) {
        List<Move> validMoves = new ArrayList<>();
        BoardPosition enemyKing = null;

        for(int i = 0; i < myBoard.boardSize; i++){
            for(int j = 0; j < myBoard.boardSize; j++){
                Piece boardPiece = myBoard.boardState[i][j].piece;

                if(boardPiece != null ){
                    if(boardPiece.isWhite != isWhite){
                        validMoves.addAll(boardPiece.canMove(myBoard.boardState[i][j], myBoard));
                    }
                    else if(boardPiece.getClass() == King.class){
                        enemyKing = myBoard.boardState[i][j];
                    }
                }
            }
        }

        for (Move move : validMoves) {
            if(move != null){
                if (move.moveTriggerPosition == enemyKing) {
                    System.out.println("Cant move there cuz dead");
                    return false;
                }
            }
        }
        return true;
    }
}
