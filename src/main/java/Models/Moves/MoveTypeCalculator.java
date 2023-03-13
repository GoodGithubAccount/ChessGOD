package Models.Moves;

import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Board.PositionChecker;
import Models.PieceModels.Pawn;
import Models.PieceModels.Piece;

import javax.swing.text.Position;
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

    Move curMove = null;

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
        int[] moveDirectionsY = {0, 0, 1, -1};

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
                    Move myMove = new Move(MoveTypes.StraightMove, startPosition, movePos, movePos, movePos, movePos);
                    if(!moveVerify(myMove, false)){
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

            if(moveToY + startPosition.y >= 0 && moveToY + startPosition.y <= 7 && moveToX + startPosition.x >= 0 && moveToX + startPosition.x <= 7){
                BoardPosition movePos = myBoard.boardState[moveToY + startPosition.y][moveToX + startPosition.x];

                Move myMove = new Move(MoveTypes.KnightMove, startPosition, movePos, movePos, movePos, movePos);
                moveVerify(myMove, false);
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

            if(moveTo + startPosition.y < 0 || moveTo + startPosition.y > 7){
                break;
            }

            BoardPosition movePos = myBoard.boardState[moveTo + startPosition.y][startPosition.x];

            MoveTypes myType;
            if(i > 0){
                myType = MoveTypes.PawnDouble;
            }
            else{
                myType = MoveTypes.PawnMove;
            }


            Move myMove = new Move(myType, startPosition, movePos, movePos, movePos, movePos);
            if(!moveVerify(myMove, false)){
                break;
            }
        }

        try {
            BoardPosition takeLeft = myBoard.boardState[direction + startPosition.y][startPosition.x - 1];
            Move myMoveLeft = new Move(MoveTypes.PawnMove, startPosition, takeLeft, takeLeft, startPosition, startPosition);
            moveVerify(myMoveLeft, true);
        }
        catch(ArrayIndexOutOfBoundsException e){
        }

        try{
            BoardPosition takeRight = myBoard.boardState[direction + startPosition.y][startPosition.x + 1];
            Move myMoveRight = new Move(MoveTypes.PawnMove, startPosition, takeRight, takeRight, startPosition, startPosition);
            moveVerify(myMoveRight, true);
        }
        catch (ArrayIndexOutOfBoundsException e){

        }
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

                moveVerify(new Move(MoveTypes.CastlingMove, startPosition, kingEndPos, move, move, rookEndPos), false);
            }
        }
    }

    public boolean moveVerify(Move myMove, boolean hasToTake){

        boolean returnVal = true;

        if(myMove.moveType != MoveTypes.CastlingMove){
            returnVal = moveVerification(myMove, hasToTake);
        }
        else{
            curMove = myMove;
        }

        if(curMove != null){
            if(myBoard.depth == 0 || myBoard.checkForMate){
                if(mateCheck(myMove)){
                    possibleMoves.add(curMove);
                }
            }
            else{
                possibleMoves.add(curMove);
            }
        }

        return returnVal;
    }

    public boolean mateCheck(Move myMove){
        Board mateCheckBoard = myBoard.cloneBoard();

        Move mateMove = new Move(myMove.moveType,
                mateCheckBoard.boardState[myMove.movePieceStartPosition.y][myMove.movePieceStartPosition.x],
        mateCheckBoard.boardState[myMove.movePieceEndPosition.y][myMove.movePieceEndPosition.x],
        mateCheckBoard.boardState[myMove.moveTriggerPosition.y][myMove.moveTriggerPosition.x],
        mateCheckBoard.boardState[myMove.otherPieceStartPosition.y][myMove.otherPieceStartPosition.x],
        mateCheckBoard.boardState[myMove.otherPieceEndPosition.y][myMove.otherPieceEndPosition.x]);

        PieceMover.movePiece(mateCheckBoard, mateMove);

        return (PositionChecker.checkForCheck(isWhite, mateCheckBoard, false));
    }

    public boolean moveVerification(Move move, boolean hasToTake){
        curMove = null;

        if(move.movePieceEndPosition.getPiece() == null){
            if(!hasToTake){
                curMove = move;
            }
            else if(move.movePieceEndPosition == myBoard.enPassant && startPosition.getPiece().getClass() == Pawn.class) {
                Move myMove = new Move(MoveTypes.EnPassant, move.movePieceStartPosition, move.movePieceEndPosition, myBoard.enPassant, myBoard.enPassantPiecePos, myBoard.enPassantPiecePos);
                curMove = myMove;
            }
            else{
            }
            return true;
        }
        else if(move.movePieceEndPosition.piece.isWhite != isWhite){
            if(move.moveType != MoveTypes.PawnMove && move.moveType != MoveTypes.PawnDouble){
                curMove = move;
            }
            return false;
        }
        else{
            return false;
        }
    }
}
