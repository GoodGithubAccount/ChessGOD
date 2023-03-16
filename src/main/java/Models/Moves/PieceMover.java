package Models.Moves;

import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.PieceModels.Pawn;
import Models.PieceModels.Piece;
import Models.PieceModels.Queen;

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

        Piece myPiece = myMove.movePieceEndPosition.piece;

        if(myPiece.getClass() == Pawn.class){
            if(myPiece.isWhite && myMove.movePieceEndPosition.y == 7){
                myMove.movePieceEndPosition.piece = new Queen(true);
            } else if (!myPiece.isWhite && myMove.movePieceEndPosition.y == 0) {
                myMove.movePieceEndPosition.piece = new Queen(false);
            }
        }
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

        if (myMove.movePieceStartPosition.piece != null) {
            myMove.movePieceStartPosition.piece.hasMoved = true;

           /*
            myMove.enPassantAtTime = myBoard.enPassant.clonePosition();
            if(myBoard.enPassant != null && myBoard.enPassant.piece != null){
                myMove.enPassantPieceAtTime = myBoard.enPassant.piece.clonePiece();
            }
            else{
                myMove.enPassantAtTime = null;
            }
            myMove.enPassantPosAtTime = myBoard.enPassantPiecePos;

            if(myMove.moveType == MoveTypes.EnPassant){
                myMove.pieceTaken = myBoard.enPassantPiecePos.piece;
            } else if (myBoard.boardState[myMove.otherPieceStartPosition.y][myMove.otherPieceStartPosition.x].piece != null) {
                myMove.pieceTaken = myBoard.boardState[myMove.otherPieceStartPosition.y][myMove.otherPieceStartPosition.x].piece;
            }
            */

            switch (myMove.moveType) {
                case EnPassant -> moveEnPassant(myBoard, myMove);
                case CastlingMove -> moveCastle(myBoard, myMove);
                case PromotionMove -> movePromotion(myBoard, myMove);
                case PawnDouble -> movePawnDouble(myBoard, myMove);
                default -> moveDef(myBoard, myMove);
            }
        }
    }

    public static void undoMove(Board myBoard, Move myMove){
        if(myMove.moveType == MoveTypes.CastlingMove){
            myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].piece = myBoard.boardState[myMove.movePieceEndPosition.y][myMove.movePieceEndPosition.x].piece;
            myBoard.boardState[myMove.otherPieceStartPosition.y][myMove.otherPieceStartPosition.x].piece = myBoard.boardState[myMove.otherPieceEndPosition.y][myMove.otherPieceEndPosition.x].piece;

            myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].piece.hasMoved = false;
            myBoard.boardState[myMove.otherPieceStartPosition.y][myMove.otherPieceStartPosition.x].piece.hasMoved = false;

            myBoard.boardState[myMove.movePieceEndPosition.y][myMove.movePieceEndPosition.x].piece = null;
            myBoard.boardState[myMove.otherPieceEndPosition.y][myMove.otherPieceEndPosition.x].piece = null;

        }
        else{
            myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].setPiece(myBoard.boardState[myMove.movePieceEndPosition.y][myMove.movePieceEndPosition.x].piece);

            myBoard.enPassant = myMove.enPassantAtTime;
            if(myBoard.enPassant != null){
                myBoard.enPassant.piece = myMove.enPassantPieceAtTime;
                myBoard.enPassantPiecePos = myMove.movePieceEndPosition;
            }

            if(myMove.moveType == MoveTypes.EnPassant){
                myBoard.boardState[myMove.moveTriggerPosition.y][myMove.moveTriggerPosition.x].piece = null;
                myBoard.boardState[myMove.otherPieceStartPosition.y][myMove.otherPieceStartPosition.x].piece = myMove.pieceTaken;
            }
            else if(myMove.pieceTaken != null){
                myBoard.boardState[myMove.moveTriggerPosition.y][myMove.moveTriggerPosition.x].piece = myMove.pieceTaken;
            }
            else{
                myBoard.boardState[myMove.moveTriggerPosition.y][myMove.moveTriggerPosition.x].piece = null;
            }

            if(myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].piece.getClass() == Pawn.class){
                if(myMove.movePieceStartPosition.y == 1 && myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].piece.isWhite){
                    myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].piece.hasMoved = false;
                }
                else if(myMove.movePieceStartPosition.y == 6 && !myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].piece.isWhite){
                    myBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x].piece.hasMoved = false;
                }
            }
        }
    }
}
