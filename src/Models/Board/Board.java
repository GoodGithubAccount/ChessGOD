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
                            newPiece = new Queen(isWhite, owner);
                            break;
                        case "K":
                            newPiece = new King(isWhite, owner);
                            break;
                        case "R":
                            newPiece = new Rook(isWhite, owner);
                            break;
                        case "B":
                            newPiece = new Bishop(isWhite, owner);
                            break;
                        case "N":
                            newPiece = new Knight(isWhite, owner);
                            break;
                        case "P":
                            newPiece = new Pawn(isWhite, owner);
                            break;
                    }
                }
                boardState[i][j] = new BoardPosition(i, j, newPiece);
            }
        }

        printBoard();
    }

    public void movePiece(BoardPosition startPosition, BoardPosition endPosition) {
        List<BoardPosition> validMoves = startPosition.piece.canMove(boardState, startPosition);

        boolean isValidMove = false;

        for (BoardPosition move : validMoves) {
            if (move.equals(endPosition)) {
                isValidMove = true;
                break;
            }
        }

        if (isValidMove) {
            endPosition.setPiece(startPosition.getPiece());
            startPosition.setPiece(null);
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

                System.out.print(pieceTag + " ");
            }
            System.out.println();
        }
    }
}
