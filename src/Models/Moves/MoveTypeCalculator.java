package Models.Moves;

import Models.Board.Board;
import Models.Board.BoardPosition;

import java.util.ArrayList;
import java.util.List;

public class MoveTypeCalculator {

    MoveTypes[] moveTypes;
    boolean isWhite;
    int moveLimit = 0;

    List<BoardPosition> possibleMoves = new ArrayList<>();
    BoardPosition[][] boardState;
    BoardPosition startPosition;

    boolean hasMoved;

    public MoveTypeCalculator(MoveTypes[] moveTypes, boolean isWhite, BoardPosition[][] boardState, BoardPosition startPosition, int moveLimit, boolean hasMoved){
        this.moveTypes = moveTypes;
        this.isWhite = isWhite;
        this.boardState = boardState;
        this.startPosition = startPosition;
        this.moveLimit = moveLimit;
        this.hasMoved = hasMoved;
    }

    public List<BoardPosition> calculateMoves(){
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
                    movePos = boardState[moveToY + startPosition.y][moveToX + startPosition.x];
                }
                catch (ArrayIndexOutOfBoundsException e){

                }

                if(movePos != null){
                    if(!moveVerification(movePos, false)){
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

            BoardPosition movePos = boardState[moveToY + startPosition.y][moveToX + startPosition.x];

            if(movePos.x >= 0 && movePos.x <= 7 && movePos.y >= 0 && movePos.y <= 7){
                moveVerification(movePos, false);
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

            BoardPosition movePos = boardState[moveTo + startPosition.y][startPosition.x];
            System.out.println("Trynna move to " + (startPosition.x) + "-" + (moveTo + startPosition.y));

            if(!moveVerification(movePos, false)){
                break;
            }
        }

        BoardPosition takeLeft = boardState[direction + startPosition.y][startPosition.x - 1];
        BoardPosition takeRight = boardState[direction + startPosition.y][startPosition.x + 1];

        moveVerification(takeLeft, true);
        moveVerification(takeRight, true);
    }
    public void castlingCheck(){
        if(!hasMoved){
            BoardPosition kingSideRook = null;
            BoardPosition queenSideRook = null;

            try{
                kingSideRook = boardState[startPosition.y][startPosition.x + 3];
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            try{
                queenSideRook = boardState[startPosition.y][startPosition.x - 4];
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            if(kingSideRook != null && kingSideRook.piece != null && !kingSideRook.piece.hasMoved){
                boolean canMove = true;
                for(int i = startPosition.x + 1; i < startPosition.x + 3; i++){
                    BoardPosition pos = boardState[startPosition.y][i];
                    if(pos.piece != null){
                        canMove = false;
                    }
                }

                if(canMove){
                    possibleMoves.add(kingSideRook);
                }
            }

            if(queenSideRook != null && queenSideRook.piece != null && !queenSideRook.piece.hasMoved){
                boolean canMove = true;
                for(int i = startPosition.x - 1; i > startPosition.x - 4; i--){
                    BoardPosition pos = boardState[startPosition.y][i];
                    if(pos.piece != null){
                        canMove = false;
                    }
                }

                if(canMove){
                    possibleMoves.add(queenSideRook);
                }
            }
        }
    }

    public boolean moveVerification(BoardPosition movePos, boolean hasToTake){
        if(movePos.piece == null){
            if(!hasToTake){
                possibleMoves.add(movePos);
            }
            return true;
        }
        else if(movePos.piece.isWhite != isWhite){
            possibleMoves.add(movePos);
            return false;
        }
        else{
            return false;
        }
    }
}
