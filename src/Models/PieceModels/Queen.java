package Models.PieceModels;

import Data.Settings;
import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Moves.MoveTypeCalculator;
import Models.Moves.MoveTypes;
import Models.Player.Player;

import java.util.List;

public class Queen extends Piece{

    MoveTypes[] myMoveTypes = {MoveTypes.DiagonalMove, MoveTypes.StraightMove};
    public Queen(boolean isWhite, Player owner, String pieceTag) {
        super(isWhite, owner, pieceTag, Settings.QUEEN_MOVE_LIMIT);
    }

    @Override
    public List<BoardPosition> canMove(BoardPosition[][] boardState, BoardPosition startPosition) {
        MoveTypeCalculator myMoveBrain = new MoveTypeCalculator(myMoveTypes, isWhite, boardState, startPosition, moveLimit);

        return myMoveBrain.calculateMoves();
    }
}
