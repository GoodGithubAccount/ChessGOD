package Models.Board;

import Models.PieceModels.Piece;

public class BoardPosition {

    public int x;
    public int y;
    public Piece piece;

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

    public BoardPosition clonePosition(){
        Piece clonePiece = null;

        if(piece != null){
            clonePiece = piece.clonePiece();
        }

        BoardPosition newBoardPos = new BoardPosition(x,y, clonePiece);

        return newBoardPos;
    }

}
