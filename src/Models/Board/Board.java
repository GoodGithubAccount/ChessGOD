package Models.Board;

import Data.Settings;
import Models.PieceModels.Piece;
import Models.PieceModels.Queen;
import Models.Player.Player;

public class Board {

    private int boardSize = Settings.BOARD_SIZE;

    private BoardPosition[][] boardState = new BoardPosition[boardSize][boardSize];

    private Player white;
    private Player black;

    public Board(Player white, Player black){
        this.white = white;
        this.black = black;

        fillBoard();
    }

    void fillBoard(){
        String[] pieceRows = Settings.BOARD_STATE.split("/");

        for(int i = 0; i < pieceRows.length; i++){
            String[] rowPieces = pieceRows[i].split("-");

            for(int j = 0; j < pieceRows.length; j++){
                String pieceTag = rowPieces[j];
                String[] colorAndPiece = pieceTag.split("[bwe]");
                Player owner = black;
                Boolean isWhite = false;

                if(colorAndPiece[0] == "w"){
                    isWhite = true;
                    owner = white;
                }

                Piece newPiece = null;

                if(colorAndPiece[0] != "e"){
                    switch(colorAndPiece[1]){
                        case "Q":
                            newPiece = new Queen(isWhite,owner,pieceTag);
                            break;
                        case "K":
                            newPiece = new Queen(isWhite,owner,pieceTag);
                            break;
                        case "R":
                            newPiece = new Queen(isWhite,owner,pieceTag);
                            break;
                        case "B":
                            newPiece = new Queen(isWhite,owner,pieceTag);
                            break;
                        case "N":
                            newPiece = new Queen(isWhite,owner,pieceTag);
                            break;
                        case "P":
                            newPiece = new Queen(isWhite,owner,pieceTag);
                            break;
                    }
                }
                boardState[i][j] = new BoardPosition(i, j, newPiece);
            }
        }

        printBoard();
    }

    void printBoard(){
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                Piece myPiece = boardState[i][j].piece;
                if(myPiece != null){
                    System.out.print(myPiece.pieceTag + " ");
                }
                else{
                    System.out.print("E ");
                }
            }
            System.out.println();
        }
    }
}
