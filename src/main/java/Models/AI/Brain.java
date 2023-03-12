package Models.AI;

import Models.Board.Board;
import Models.Board.PositionChecker;
import Models.Moves.Move;
import Models.Moves.PieceMover;
import Models.PieceModels.Piece;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Brain {

    int depth;
    boolean isGood;
    List<Move> savedMove = new ArrayList<>();
    int savedValuation = 0;

    public Brain(int depth){
        this.depth = depth;
    }

    public void recommendMove(boolean isWhite, Board myBoard){
        savedMove = null;
        savedValuation = 0;

        testMove(isWhite, myBoard);

        System.out.println("EVALUATION BEST:" + savedValuation);
        for (Move move : savedMove){
            if(move != null){
                System.out.println("MOVE : " + (move.movePieceStartPosition.x + 1) + "," + (move.movePieceStartPosition.y + 1) + "-"
                + (move.moveTriggerPosition.x + 1) + "," + (move.moveTriggerPosition.y + 1));
            }
        }
    }

    public void testMove(boolean isWhite, Board myBoard){
        List<Move> moves = PositionChecker.getMoves(!isWhite, myBoard);

        for (Move move : moves) {
            if(move != null){
                evaluatePosition(isWhite, myBoard, move, 0, 0, new ArrayList<>());
            }
        }
    }

    public int evaluatePosition(boolean isWhite, Board myBoardTT, Move moveT, int curDepth, int valuation, List<Move> moveBank){
        List<Move> newMoveBank = new ArrayList<>(moveBank);
        Move newMove = moveT.cloneMove();

        newMoveBank.add(newMove);
        curDepth++;

        if(valuation > savedValuation){
            savedValuation = valuation;
            savedMove = newMoveBank;
        }

        if(curDepth > 5){
            return valuation;
        }

        Board myBoard = myBoardTT.cloneBoard();
        PieceMover.movePiece(myBoard, newMove);

        List <Move> allMoves = new ArrayList<>();

        for(int i = 0; i < myBoard.boardSize; i++){
            for(int j = 0; j < myBoard.boardSize; j++){
                Piece myPiece = myBoard.boardState[i][j].piece;
                if(myPiece != null && myPiece.isWhite == isWhite){
                    allMoves.addAll(myPiece.canMove(myBoard.boardState[i][j],myBoard));
                }
            }
        }

        boolean attack = false;
        int otherAttackVal = 0;
        Piece curPiece = null;

        int moveAmounts = 0;

        for (Move move : allMoves) {
            if(move != null && myBoard.boardState[move.movePieceStartPosition.y][move.movePieceStartPosition.x].piece != null){

                int moveVal = 0;

                Piece movePiece = myBoard.boardState[move.movePieceStartPosition.y][move.movePieceStartPosition.x].piece;
                Piece otherPiece = myBoard.boardState[move.movePieceEndPosition.y][move.movePieceEndPosition.x].piece;

                if(curPiece != movePiece){
                    // Save values

                    otherAttackVal = 0;
                    attack = false;
                    valuation = 0;
                }
                else{
                    curPiece = movePiece;
                }

                if(otherPiece != null){
                    if(movePiece.isWhite == otherPiece.isWhite)
                    {
                        moveVal = 100;
                    }
                    else if(movePiece.isWhite != otherPiece.isWhite){
                        moveVal = otherPiece.pieceValue;

                        if(attack){
                            moveVal += otherAttackVal;

                            if(otherAttackVal < moveVal){
                                otherAttackVal = moveVal;
                            }
                        }

                        attack = true;
                    }
                }
                else{
                    moveAmounts++;
                }

                moveVal += evaluatePosition(!isWhite,myBoard,move,curDepth,moveVal, newMoveBank);

                if(moveAmounts > moveVal){
                    moveVal = moveAmounts;
                }

                if(isWhite != isGood){
                    valuation -= moveVal;
                }
                else{
                    if(moveVal > valuation){
                        valuation += moveVal;
                    }
                }
            }
        }

        return valuation;
    }
}
