package Models.Board;

import Data.Settings;
import Models.PieceModels.*;
import Models.Player.Player;

import java.util.List;

public class Board {

    public int boardSize = Settings.BOARD_SIZE;
    public BoardPosition[][] boardState = new BoardPosition[boardSize][boardSize];
    public BoardPosition enPassant = null;
    public BoardPosition enPassantPiecePos = null;
    public int depth = 0;

    public Board() {
    }

    public void fillBoard() {
        String[] pieceRows = Settings.BOARD_STATE.split("/");

        for (int i = 0; i < pieceRows.length; i++) {
            String[] rowPieces = pieceRows[pieceRows.length - i - 1].split("-");

            for (int j = 0; j < pieceRows.length; j++) {
                String pieceTag = rowPieces[j];

                char color = pieceTag.charAt(0);
                String piece = pieceTag.substring(1);

                Piece newPiece = null;
                if (color != 'e') {

                    Player owner;
                    Boolean isWhite;

                    if(color == 'w') {
                        isWhite = true;
                    }
                    else {
                        isWhite = false;
                    }

                    switch (piece) {
                        case "Q":
                            newPiece = new Queen(isWhite);
                            break;
                        case "K":
                            newPiece = new King(isWhite);
                            break;
                        case "R":
                            newPiece = new Rook(isWhite);
                            break;
                        case "B":
                            newPiece = new Bishop(isWhite);
                            break;
                        case "N":
                            newPiece = new Knight(isWhite);
                            break;
                        case "P":
                            newPiece = new Pawn(isWhite);
                            break;
                    }
                }
                boardState[i][j] = new BoardPosition(j, i, newPiece);
            }
        }

        printBoard();
    }

    public void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                String pieceTag = "";

                Piece myPiece = boardState[boardSize - i - 1][j].piece;
                if (myPiece != null) {
                    if (myPiece.isWhite) {
                        pieceTag += "w";
                    } else {
                        pieceTag += "b";
                    }

                    // Cant decide if this should be handled in the object instead.
                    // For now sticking with this solution for prototype.
                    if (myPiece.getClass() == Queen.class) {
                        pieceTag += "Q";
                    }
                    else if (myPiece.getClass() == King.class) {
                        pieceTag += "K";
                    }
                    else if (myPiece.getClass() == Pawn.class) {
                        pieceTag += "P";
                    }
                    else if (myPiece.getClass() == Bishop.class) {
                        pieceTag += "B";
                    }
                    else if (myPiece.getClass() == Rook.class) {
                        pieceTag += "R";
                    }
                    else if (myPiece.getClass() == Knight.class) {
                        pieceTag += "N";
                    }

                } else {
                    pieceTag = "eE";
                }

                System.out.print(pieceTag + " " + (j + 1) + "-" + (boardSize - i) + "   ");
            }
            System.out.println();
        }
    }

    public Board cloneBoard() {
        Board myClone = new Board();
        if(enPassant != null){
            myClone.enPassant = new BoardPosition(enPassant.x, enPassant.y, enPassant.piece);
            myClone.enPassantPiecePos = new BoardPosition(enPassantPiecePos.x, enPassantPiecePos.y, enPassantPiecePos.piece.clonePiece());
        }
        else{
            myClone.enPassant = null;
            myClone.enPassantPiecePos = null;
        }

        myClone.depth = depth + 1;

        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                Piece myPiece = boardState[i][j].piece;
                if(myPiece != null){
                    myPiece = myPiece.clonePiece();
                }
                myClone.boardState[i][j] = new BoardPosition(boardState[i][j].x, boardState[i][j].y, myPiece);
            }
        }

        return myClone;
    }
}
