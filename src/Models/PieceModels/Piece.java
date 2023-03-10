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

    public Piece(boolean isWhite, Player owner, int moveLimit){
        this.isWhite = isWhite;
        this.owner = owner;
        this.moveLimit = moveLimit;
    }

    public abstract List<BoardPosition> canMove(BoardPosition[][] boardState, BoardPosition startPosition);

}
