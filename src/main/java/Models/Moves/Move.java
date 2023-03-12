package Models.Moves;

import Models.Board.Board;
import Models.Board.BoardPosition;

public class Move {
    public MoveTypes moveType;
    public BoardPosition movePieceStartPosition;
    public BoardPosition movePieceEndPosition;

    public BoardPosition moveTriggerPosition;

    public BoardPosition otherPieceStartPosition;
    public BoardPosition otherPieceEndPosition;


    public Move(MoveTypes moveType, BoardPosition movePieceStartPosition, BoardPosition movePieceEndPosition, BoardPosition moveTriggerPosition, BoardPosition otherPieceStartPosition, BoardPosition otherPieceEndPosition) {
        this.moveType = moveType;
        this.movePieceStartPosition = movePieceStartPosition;
        this.movePieceEndPosition = movePieceEndPosition;
        this.moveTriggerPosition = moveTriggerPosition;
        this.otherPieceStartPosition = otherPieceStartPosition;
        this.otherPieceEndPosition = otherPieceEndPosition;
    }

    public Move cloneMove(){
        Move newMove = new Move(moveType,movePieceStartPosition.clonePosition(),movePieceEndPosition.clonePosition(),moveTriggerPosition.clonePosition(),otherPieceStartPosition.clonePosition(), otherPieceEndPosition.clonePosition());

        return newMove;
    }
}
