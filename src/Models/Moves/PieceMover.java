package Models.Moves;

import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.PieceModels.Pawn;

import java.util.List;

public class PieceMover {

    public static void moveEnPassant(Board myBoard, Move myMove){
        myBoard.enPassantPiecePos.setPiece(null);
        myBoard.enPassant = null;

        myBoard.boardState[myMove.movePieceEndPosition.y][myMove.movePieceEndPosition.x].setPiece(myMove.movePieceStartPosition.getPiece());
        myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].setPiece(null);
    }

    public static void movePawnDouble(Board myBoard, Move myMove){
        int calculateDiff = myMove.movePieceStartPosition.y - myMove.movePieceEndPosition.y;

        myBoard.enPassant = myBoard.boardState[myMove.movePieceStartPosition.y - (calculateDiff / 2)][myMove.movePieceStartPosition.x];
        myBoard.enPassantPiecePos = myMove.movePieceEndPosition;

        moveDef(myBoard, myMove);
    }

    public static void moveDef(Board myBoard, Move myMove){
        myMove.movePieceEndPosition.setPiece(myMove.movePieceStartPosition.piece);
        myMove.movePieceStartPosition.setPiece(null);
    }

    public static void moveCastle(Board myBoard, Move myMove){
        myMove.movePieceEndPosition.setPiece(myMove.movePieceStartPosition.getPiece());
        myMove.movePieceStartPosition.setPiece(null);

        myMove.otherPieceEndPosition.setPiece(myMove.moveTriggerPosition.getPiece());
        myMove.otherPieceStartPosition.setPiece(null);
    }

    public static void movePromotion(Board myBoard, Move myMove){

    }
    public static void movePiece(Board myBoard, Move myMove) {

        myMove.movePieceStartPosition.getPiece().hasMoved = true;

        switch(myMove.moveType){
            case EnPassant -> moveEnPassant(myBoard, myMove);
            case CastlingMove -> moveCastle(myBoard, myMove);
            case PromotionMove -> movePromotion(myBoard, myMove);
            case PawnDouble -> movePawnDouble(myBoard, myMove);
            default -> moveDef(myBoard, myMove);
        }
    }

}
