package Models.PieceModels;

import Data.Settings;
import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Moves.MoveTypeCalculator;
import Models.Moves.MoveTypes;
import Models.Player.Player;

import java.util.List;

public class Pawn extends Piece{

    MoveTypes[] myMoveTypes = {MoveTypes.PawnMove};
    public Pawn(boolean isWhite, Player owner, Board myBoard) {
        super(isWhite, owner, Settings.PAWN_MOVE_LIMIT, myBoard);
    }

    @Override
    public List<BoardPosition> canMove(BoardPosition[][] boardState, BoardPosition startPosition, Board myBoard) {
        MoveTypeCalculator myMoveBrain = new MoveTypeCalculator(myMoveTypes, isWhite, boardState, startPosition, moveLimit, hasMoved, myBoard);

        return myMoveBrain.calculateMoves();
    }
}
