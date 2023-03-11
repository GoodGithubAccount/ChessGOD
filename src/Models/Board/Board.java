package Models.Board;

import Data.Settings;
import Models.PieceModels.*;
import Models.Player.Player;

import java.util.List;

public class Board {

    private int boardSize = Settings.BOARD_SIZE;

    public BoardPosition[][] boardState = new BoardPosition[boardSize][boardSize];

    private Player white;
    private Player black;

    public BoardPosition enPassant = null;
    public BoardPosition enPassantPiecePos = null;

    public Board(Player white, Player black) {
        this.white = white;
        this.black = black;

        fillBoard();
    }

    void fillBoard() {
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
                        owner = white;
                    }
                    else {
                        isWhite = false;
                        owner = black;
                    }

                    switch (piece) {
                        case "Q":
                            newPiece = new Queen(isWhite, owner, this);
                            break;
                        case "K":
                            newPiece = new King(isWhite, owner, this);
                            break;
                        case "R":
                            newPiece = new Rook(isWhite, owner, this);
                            break;
                        case "B":
                            newPiece = new Bishop(isWhite, owner, this);
                            break;
                        case "N":
                            newPiece = new Knight(isWhite, owner, this);
                            break;
                        case "P":
                            newPiece = new Pawn(isWhite, owner, this);
                            break;
                    }
                }
                boardState[i][j] = new BoardPosition(j, i, newPiece);
            }
        }

        printBoard();
    }

    public void movePiece(BoardPosition startPosition, BoardPosition endPosition) {
        List<BoardPosition> validMoves = startPosition.piece.canMove(boardState, startPosition, this);

        boolean isValidMove = false;

        for (BoardPosition move : validMoves) {
            if (move.equals(endPosition)) {
                isValidMove = true;
                break;
            }
        }

        if (isValidMove) {
            if(!startPosition.getPiece().hasMoved){
                startPosition.getPiece().hasMoved = true;
            }

            // If it's a castling move
            if(endPosition.piece != null && startPosition.getPiece().isWhite == endPosition.getPiece().isWhite){
                int xModifier;
                if(startPosition.x < endPosition.x){
                    xModifier = 2;
                }
                else{
                    xModifier = -2;
                }

                boardState[startPosition.y][startPosition.x + xModifier].setPiece(startPosition.getPiece());
                startPosition.setPiece(null);

                boardState[startPosition.y][startPosition.x + (xModifier / 2)].setPiece(endPosition.getPiece());
                endPosition.setPiece(null);

                enPassant = null;
            }
            else{ // En passant
                if(startPosition.getPiece().getClass() == Pawn.class){
                    int calculateDiff = startPosition.y - endPosition.y;

                    if(Math.abs(calculateDiff) == 2){
                        enPassant = boardState[startPosition.y - (calculateDiff / 2)][startPosition.x];
                        enPassantPiecePos = endPosition;
                    }
                    else if(endPosition.equals(enPassant)){
                        enPassantPiecePos.setPiece(null);
                        enPassant = null;
                    }

                    endPosition.setPiece(startPosition.getPiece());
                    startPosition.setPiece(null);
                }
                else{ // Normal move
                    endPosition.setPiece(startPosition.getPiece());
                    startPosition.setPiece(null);

                    enPassant = null;
                }
            }
        } else {
            System.out.println("Invalid move");
        }
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
}
