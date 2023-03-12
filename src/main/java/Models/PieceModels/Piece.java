package Models.PieceModels;

import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Moves.Move;
import Models.Moves.MoveTypes;
import Models.Player.Player;

import java.util.List;

public abstract class Piece {

    public boolean isWhite = false;
    public boolean hasMoved = false;
    public int moveLimit = 0;
    public int pieceValue = 0;
    public MoveTypes[] myMoveTypes;
    public Piece(boolean isWhite, int moveLimit, MoveTypes[] myMoveTypes, int pieceValue){
        this.isWhite = isWhite;
        this.moveLimit = moveLimit;
        this.myMoveTypes = myMoveTypes;
        this.pieceValue = pieceValue;
    }

    public abstract List<Move> canMove(BoardPosition startPosition, Board myBoard);
    public abstract Piece clonePiece();
}
