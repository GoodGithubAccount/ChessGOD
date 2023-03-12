package Models.Board;

import Models.Moves.Move;
import Models.Moves.PieceMover;
import Models.PieceModels.King;
import Models.PieceModels.Piece;

import java.util.ArrayList;
import java.util.List;

public class PositionChecker {

    // Checks for checks
    // Both used to check if player is checked, and if player will be checked if they move, thus move would be illegal
    public static boolean checkForCheck(boolean isWhite, Board myBoard, boolean toPrint) {
        List<Move> validMoves = new ArrayList<>();
        List<Move> kingSideValidMoves = new ArrayList<>();
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
                        validMoves.addAll(boardPiece.canMove(myBoard.boardState[i][j], myBoard));
                    }
                    else{
                        kingSideValidMoves.addAll(boardPiece.canMove(myBoard.boardState[i][j], myBoard));
                    }
                }
            }
        }

        boolean check = false;
        boolean noMoves = true;

        for (Move move : validMoves) {
            if(move != null){
                if (move.moveTriggerPosition == enemyKing) {
                    System.out.println("Move coming from - " +
                            move.movePieceStartPosition.x + "," + move.movePieceStartPosition.y + "-" +
                            move.moveTriggerPosition.x + "," + move.moveTriggerPosition.y);
                    check = true;
                }
            }
        }

        for (Move move : kingSideValidMoves) {
            if(move != null){
                noMoves = false;
            }
        }

        if(noMoves && check){
            if(toPrint){
                System.out.println("CHECKMATE GAME OVER");
            }
        }
        else if(noMoves && !check){
            if(toPrint){
                System.out.println("STALEMATE GAME OVER");
            }
        }
        else if(check && !noMoves){
            if(toPrint){
                System.out.println("CHECK");
            }
            return false;
        }

        return true;
    }
}
