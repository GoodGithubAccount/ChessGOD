package Models.Moves;

import Models.Board.Board;
import Models.Board.BoardPosition;

import java.util.Dictionary;
import java.util.List;

public class MoveTypeCalculator {

    MoveTypes[] moveTypes;
    boolean isWhite;
    int moveLimit = 0;

    List<BoardPosition> possibleMoves;
    BoardPosition[][] boardState;
    BoardPosition startPosition;

    public MoveTypeCalculator(MoveTypes[] moveTypes, boolean isWhite, BoardPosition[][] boardState, BoardPosition startPosition, int moveLimit){
        this.moveTypes = moveTypes;
        this.isWhite = isWhite;
        this.boardState = boardState;
        this.startPosition = startPosition;
        this.moveLimit = moveLimit;
    }

    public List<BoardPosition> calculateMoves(){
        for(int i = 0; i < moveTypes.length; i++){
            switch(moveTypes[i]){
                case CastlingMove -> castlingCheck();
                case PawnMove -> pawnMoveCheck(moveLimit);
                case PawnTake -> pawnTakeCheck();
                case KnightMove -> knightMoveCheck();
                case DiagonalMove -> diagonalMoveCheck(moveLimit);
                case StraightMove -> straightMoveCheck(moveLimit);
            }
        }

        return possibleMoves;
    }
    public void straightMoveCheck(int moveLimit){

    }
    public void diagonalMoveCheck(int moveLimit){

    }
    public void knightMoveCheck(){

    }
    public void pawnTakeCheck(){

    }
    public void pawnMoveCheck(int moveLimit){
        int direction = 1;

        if(isWhite){
            direction = 1;
        }
        else{
            direction = -1;
        }

        int moveTo = direction;

        for(int i = 0; i < moveLimit; i++){
            BoardPosition movePos = boardState[moveTo + startPosition.y][startPosition.x];

            if(!verifyKeepMoving(movePos)){
                break;
            }
            
            moveTo += direction;
        }
    }
    public void castlingCheck(){

    }

    public boolean verifyKeepMoving(BoardPosition movePos){
        if(movePos.piece == null){
            possibleMoves.add(movePos);
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
