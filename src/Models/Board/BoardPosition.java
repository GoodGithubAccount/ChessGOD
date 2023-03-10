package Models.Board;

import Models.PieceModels.Piece;

public class BoardPosition {

    public int x;
    public int y;
    public Piece piece = null;

    public BoardPosition(int x, int y, Piece piece){
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

}
