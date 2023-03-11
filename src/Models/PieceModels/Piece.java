package Models.PieceModels;

import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Player.Player;

import java.util.List;

public abstract class Piece {

    private Player owner = null;
    public boolean isWhite = false;
    public boolean hasMoved = false;

    int moveLimit = 0;

    Board myBoard;

    public Piece(boolean isWhite, Player owner, int moveLimit, Board myBoard){
        this.isWhite = isWhite;
        this.owner = owner;
        this.moveLimit = moveLimit;
        this.myBoard = myBoard;
    }

    public abstract List<BoardPosition> canMove(BoardPosition[][] boardState, BoardPosition startPosition, Board myBoard);

}
