package Models.Board;

import Models.Moves.Move;
import Models.Moves.PieceMover;
import Models.PieceModels.King;
import Models.PieceModels.Piece;

import java.util.ArrayList;
import java.util.List;

public class PositionChecker {

    public static List<Move> getMoves(boolean isWhite, Board myBoard){
        List<Move> validMoves = new ArrayList<>();

        for(int i = 0; i < myBoard.boardSize; i++){
            for(int j = 0; j < myBoard.boardSize; j++){
                Piece boardPiece = myBoard.boardState[i][j].piece;

                if(boardPiece != null ){
                    if(boardPiece.isWhite != isWhite){
                        validMoves.addAll(boardPiece.canMove(myBoard.boardState[i][j], myBoard));
                    }
                }
            }
        }

        return validMoves;
    }

    public static BoardPosition getKing(boolean isWhite, Board myBoard){
        BoardPosition enemyKing = null;
        for(int i = 0; i < myBoard.boardSize; i++){
            if(enemyKing != null){
                break;
            }

            for(int j = 0; j < myBoard.boardSize; j++){
                Piece boardPiece = myBoard.boardState[i][j].piece;

                if(boardPiece != null ){
                    if(boardPiece.getClass() == King.class && boardPiece.isWhite == isWhite){
                        enemyKing = myBoard.boardState[i][j];
                        break;
                    }
                }
            }
        }

        return enemyKing;
    }

    public static boolean isCheck(boolean isWhite, Board myBoard, boolean toPrint){
        List<Move> validMoves = getMoves(isWhite, myBoard);
        BoardPosition enemyKing = getKing(isWhite, myBoard);

        for (Move move : validMoves) {
            if(move != null){
                if (move.moveTriggerPosition == enemyKing) {
                    if(toPrint){
                        System.out.println("Move coming from - " +
                                move.movePieceStartPosition.x + "," + move.movePieceStartPosition.y + "-" +
                                move.moveTriggerPosition.x + "," + move.moveTriggerPosition.y);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isMate(boolean isWhite, Board myBoard){
        List<Move> validMoves = getMoves(!isWhite, myBoard);

        boolean mate = true;

        for (Move move : validMoves) {
            if(move != null){
                Board mateCheckBoard = myBoard.cloneBoard();

                Move mateMove = new Move(move.moveType,
                        mateCheckBoard.boardState[move.movePieceStartPosition.y][move.movePieceStartPosition.x],
                        mateCheckBoard.boardState[move.movePieceEndPosition.y][move.movePieceEndPosition.x],
                        mateCheckBoard.boardState[move.moveTriggerPosition.y][move.moveTriggerPosition.x],
                        mateCheckBoard.boardState[move.otherPieceStartPosition.y][move.otherPieceStartPosition.x],
                        mateCheckBoard.boardState[move.otherPieceEndPosition.y][move.otherPieceEndPosition.x]);

                PieceMover.movePiece(mateCheckBoard, mateMove);

                if(!isCheck(isWhite, mateCheckBoard, false)){
                    System.out.println("Not mate because " + (move.movePieceStartPosition.x + 1) + "-" + (move.movePieceEndPosition.y + 1) + "-"
                    + (move.moveTriggerPosition.x + 1) + "-" + (move.moveTriggerPosition.y + 1));
                    System.out.println(mateCheckBoard.boardState[move.movePieceEndPosition.y][move.movePieceEndPosition.x].piece.getClass());

                    mate = false;
                }
            }
        }

        return mate;
    }

    public static boolean isStalemate(boolean isWhite, Board myBoard){
        List<Move> avoidMoves = getMoves(!isWhite, myBoard);

        boolean stalemate = true;

        for (Move move : avoidMoves) {
            if(move != null){
                stalemate = false;
            }
        }

        return stalemate;
    }

    public static boolean checkCheck(boolean isWhite, Board myBoard, boolean toPrint){
        boolean keepPlaying = false;

        if(isCheck(isWhite, myBoard, toPrint)){
            if(isMate(isWhite, myBoard)){
                if(toPrint){
                    System.out.println("CHECKMATE");
                }
            }
            else{
                if(toPrint){
                    System.out.println("CHECK");
                }
            }
        }
        else if(isStalemate(isWhite, myBoard)){
            if(toPrint){
                System.out.println("STALEMATE");
            }
        }
        else{
            keepPlaying = true;
        }

        return keepPlaying;
    }

    // Checks for checks
    // Both used to check if player is checked, and if player will be checked if they move, thus move would be illegal
    public static boolean checkForCheck(boolean isWhite, Board myBoard, boolean toPrint) {
        return checkCheck(isWhite, myBoard, toPrint);
    }
}
