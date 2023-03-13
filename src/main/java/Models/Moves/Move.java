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

    public Move cloneMove(Board myBoard){
        BoardPosition startPos = myBoard.boardState[movePieceStartPosition.y][movePieceStartPosition.x];
        BoardPosition endPos = myBoard.boardState[movePieceEndPosition.y][movePieceEndPosition.x];
        BoardPosition triggerPos = myBoard.boardState[moveTriggerPosition.y][moveTriggerPosition.x];
        BoardPosition otherStartPos = myBoard.boardState[otherPieceStartPosition.y][otherPieceStartPosition.x];
        BoardPosition otherEndPos = myBoard.boardState[otherPieceEndPosition.y][otherPieceEndPosition.x];

        Move newMove = new Move(moveType,startPos,endPos,triggerPos,otherStartPos, otherEndPos);

        return newMove;
    }
}
