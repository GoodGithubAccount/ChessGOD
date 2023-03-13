package Models.AI;

import Models.Board.Board;
import Models.Board.PositionChecker;
import Models.Moves.Move;
import Models.Moves.MoveEvaluation;
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

    MoveEvaluation bestEVAL = new MoveEvaluation(new ArrayList<>(), 0);

    public Brain(int depth){
        this.depth = depth;
    }

    public void recommendMove(boolean isWhite, Board myBoard){

        isGood = isWhite;

        bestEVAL = new MoveEvaluation(new ArrayList<>(), 0);

        MoveEvaluation bestVal = testMove2(isWhite, myBoard, 0, new MoveEvaluation(new ArrayList<>(), 0));

        savedMove = bestEVAL.moves;
        savedValuation = bestEVAL.valuation;

        System.out.println("EVALUATION BEST:" + savedValuation);
        for (Move move : savedMove){
            if(move != null){
                System.out.println("MOVE : " + (move.movePieceStartPosition.x + 1) + "," + (move.movePieceStartPosition.y + 1) + "-"
                + (move.moveTriggerPosition.x + 1) + "," + (move.moveTriggerPosition.y + 1));
            }
        }
    }
    public MoveEvaluation testMove2(boolean isWhite, Board myBoard, int curDepth, MoveEvaluation valAy){
        MoveEvaluation bestEvaluation = valAy.clone();

        MoveEvaluation tempVal = evaluatePosition(isWhite, myBoard, curDepth, valAy.clone());

        if(tempVal.valuation >= bestEvaluation.valuation){
            bestEvaluation = tempVal.clone();
        }

        if(bestEvaluation.valuation > bestEVAL.valuation || (bestEvaluation.valuation >= bestEVAL.valuation && bestEvaluation.moves.size() >= bestEVAL.moves.size())){
            bestEVAL = bestEvaluation.clone();
        }

        return bestEvaluation.clone();
    }

    public MoveEvaluation evaluatePosition(boolean isWhite, Board myBoardTT, int curDepth, MoveEvaluation valuation){
        curDepth++;
        Board myBoard = myBoardTT.cloneBoard();

        if(curDepth > 5){
            return valuation.clone();
        }

        List <Move> allMoves = PositionChecker.getMoves(!isWhite, myBoard);

        boolean attack = false;
        int otherAttackVal = 0;
        Piece curPiece = null;

        int moveAmounts = allMoves.size();

        for (Move move : allMoves) {
            Board myBoardNEW = myBoard.cloneBoard();
            move = move.cloneMove(myBoardNEW);
            if(move != null && myBoardNEW.boardState[move.movePieceStartPosition.y][move.movePieceStartPosition.x].piece != null){
                MoveEvaluation thisVal = valuation.clone();

                int moveVal = 0;

                Piece movePiece = myBoardNEW.boardState[move.movePieceStartPosition.y][move.movePieceStartPosition.x].piece;
                Piece otherPiece = myBoardNEW.boardState[move.movePieceEndPosition.y][move.movePieceEndPosition.x].piece;

                if(curPiece != movePiece){
                    // Save values

                    moveAmounts = 0;
                    otherAttackVal = 0;
                    attack = false;
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

                moveVal += moveAmounts;

                MoveEvaluation newVal = thisVal.clone();

                if(isWhite != isGood){
                   newVal.valuation -= moveVal;
                }
                else{
                    newVal.valuation += moveVal;
                }

                newVal.moves.add(move);

                PieceMover.movePiece(myBoardNEW, move);
                testMove2(!isWhite, myBoardNEW, curDepth, newVal);
            }
        }

        return valuation.clone();
    }
}