package Models.Moves;

import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.PieceModels.Pawn;
import Models.PieceModels.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveTypeCalculator {

    MoveTypes[] moveTypes;
    boolean isWhite;
    int moveLimit = 0;

    List<Move> possibleMoves = new ArrayList<>();
    BoardPosition startPosition;
    boolean hasMoved;

    Board myBoard;

    Piece myPiece;

    public MoveTypeCalculator(BoardPosition startPosition, Board myBoard){
        this.startPosition = startPosition;

        myPiece = startPosition.getPiece();

        if(myPiece != null){
            this.moveTypes = myPiece.myMoveTypes;
            this.isWhite = myPiece.isWhite;
            this.moveLimit = myPiece.moveLimit;
            this.hasMoved = myPiece.hasMoved;
        }

        this.myBoard = myBoard;
    }

    public List<Move> calculateMoves(){
        for(int i = 0; i < moveTypes.length; i++){
            switch(moveTypes[i]){
                case CastlingMove -> castlingCheck();
                case PawnMove -> pawnMoveCheck(moveLimit);
                case KnightMove -> knightMoveCheck();
                case DiagonalMove -> diagonalMoveCheck(moveLimit);
                case StraightMove -> straightMoveCheck(moveLimit);
            }
        }

        return possibleMoves;
    }
    public void straightMoveCheck(int moveLimit){
        int[] moveDirectionsX = {1, -1, 0, 0};
        int[] moveDirectionsY = {0, 0, 1, 1};

        StraightOrDiagonalMoveCheck(moveLimit, moveDirectionsX, moveDirectionsY);
    }

    public void diagonalMoveCheck(int moveLimit){
        int[] moveDirectionsX = {1, -1, -1, 1};
        int[] moveDirectionsY = {1, -1, 1, -1};

        StraightOrDiagonalMoveCheck(moveLimit, moveDirectionsX, moveDirectionsY);
    }

    public void StraightOrDiagonalMoveCheck(int moveLimit, int[] moveDirectionsX, int[] moveDirectionsY){
        for(int i = 0; i < 4; i++){
            int moveToX = 0;
            int moveToY = 0;

            for(int j = 0; j < moveLimit; j++){
                moveToX += moveDirectionsX[i];
                moveToY += moveDirectionsY[i];

                BoardPosition movePos = null;
                try{
                    movePos = myBoard.boardState[moveToY + startPosition.y][moveToX + startPosition.x];
                }
                catch (ArrayIndexOutOfBoundsException e){

                }

                if(movePos != null){
                    Move myMove = new Move(MoveTypes.StraightMove, startPosition, movePos, movePos, null, null);
                    if(!moveVerification(myMove, false)){
                        break;
                    }
                }
            }
        }
    }
    public void knightMoveCheck(){
        int[] moveDirectionsX = {1, 2, 2, 1, -1, -2, -2, -1};
        int[] moveDirectionsY = {2, 1, -1, -2, -2, -1, 1, 2};

        for(int i = 0; i < 8; i++){
            int moveToX = moveDirectionsX[i];
            int moveToY = moveDirectionsY[i];

            BoardPosition movePos = myBoard.boardState[moveToY + startPosition.y][moveToX + startPosition.x];

            if(movePos.x >= 0 && movePos.x <= 7 && movePos.y >= 0 && movePos.y <= 7){
                Move myMove = new Move(MoveTypes.KnightMove, startPosition, movePos, movePos, null, null);
                moveVerification(myMove, false);
            }
        }
    }
    public void pawnMoveCheck(int moveLimit){
        if(!hasMoved){
            moveLimit += 1;
        }

        int direction;

        if(isWhite){
            direction = 1;
        }
        else{
            direction = -1;
        }

        int moveTo = 0;

        for(int i = 0; i < moveLimit; i++){
            moveTo += direction;

            BoardPosition movePos = myBoard.boardState[moveTo + startPosition.y][startPosition.x];
            System.out.println("Trynna move to " + (startPosition.x) + "-" + (moveTo + startPosition.y));

            MoveTypes myType;
            if(i > 0){
                myType = MoveTypes.PawnDouble;
            }
            else{
                myType = MoveTypes.PawnMove;
            }


            Move myMove = new Move(myType, startPosition, movePos, movePos, null, null);
            if(!moveVerification(myMove, false)){
                break;
            }
        }

        BoardPosition takeLeft = myBoard.boardState[direction + startPosition.y][startPosition.x - 1];
        BoardPosition takeRight = myBoard.boardState[direction + startPosition.y][startPosition.x + 1];

        Move myMoveLeft = new Move(MoveTypes.PawnMove, startPosition, takeLeft, takeLeft, null, null);
        Move myMoveRight = new Move(MoveTypes.PawnMove, startPosition, takeRight, takeRight, null, null);
        moveVerification(myMoveLeft, true);
        moveVerification(myMoveRight, true);
    }
    public void castlingCheck(){
        if(!hasMoved){
            BoardPosition kingSideRook = null;
            BoardPosition queenSideRook = null;

            try{
                kingSideRook = myBoard.boardState[startPosition.y][startPosition.x + 3];
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            try{
                queenSideRook = myBoard.boardState[startPosition.y][startPosition.x - 4];
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            List<BoardPosition> castleMoves = new ArrayList<>();

            if(kingSideRook != null && kingSideRook.piece != null && !kingSideRook.piece.hasMoved){
                boolean canMove = true;
                for(int i = startPosition.x + 1; i < startPosition.x + 3; i++){
                    BoardPosition pos = myBoard.boardState[startPosition.y][i];
                    if(pos.piece != null){
                        canMove = false;
                    }
                }

                castleMoves.add(kingSideRook);

                if(canMove){
                    castleMoves.add(kingSideRook);
                }
            }

            if(queenSideRook != null && queenSideRook.piece != null && !queenSideRook.piece.hasMoved){
                boolean canMove = true;
                for(int i = startPosition.x - 1; i > startPosition.x - 4; i--){
                    BoardPosition pos = myBoard.boardState[startPosition.y][i];
                    if(pos.piece != null){
                        canMove = false;
                    }
                }

                if(canMove){
                    castleMoves.add(queenSideRook);
                }
            }

            int xModifier;

            for (BoardPosition move : castleMoves) {
                if(startPosition.x < move.x){
                    xModifier = 2;
                }
                else{
                    xModifier = -2;
                }

                BoardPosition kingEndPos = myBoard.boardState[startPosition.y][startPosition.x + xModifier];
                BoardPosition rookEndPos = myBoard.boardState[startPosition.y][startPosition.x + (xModifier / 2)];

                moveVerify(new Move(MoveTypes.CastlingMove, startPosition, kingEndPos, move, move, rookEndPos));
            }
        }
    }

    public boolean moveVerify(Move myMove){
        if(myMove.moveType != MoveTypes.CastlingMove){

        }

        mateCheck(myMove);

        return false;
    }

    public boolean mateCheck(Move myMove){

        return false;
    }

    public boolean moveMateVerify(Move move){
        // Rework entire move handler as a new data class.

        //BoardPosition[][] mateBoard = myBoard.boardState;
        //mateBoard[movePos.y][movePos.x].piece = startPosition.getPiece();
        return false;
    }

    public boolean moveVerification(Move move, boolean hasToTake){
        if(moveMateVerify(move)){
            return false;
        }

        if(move.movePieceEndPosition.getPiece() == null){
            if(!hasToTake){
                possibleMoves.add(move);
            }
            else if(move.movePieceEndPosition == myBoard.enPassant && startPosition.getPiece().getClass() == Pawn.class) {
                Move myMove = new Move(MoveTypes.EnPassant, move.movePieceStartPosition, move.movePieceEndPosition, myBoard.enPassant, myBoard.enPassantPiecePos, myBoard.enPassantPiecePos);
                possibleMoves.add(myMove);
            }
            return true;
        }
        else if(move.movePieceEndPosition.piece.isWhite != isWhite){
            possibleMoves.add(move);
            return false;
        }
        else{
            return false;
        }
    }
}
