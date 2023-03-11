package Models.PieceModels;

import Data.Settings;
import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Moves.Move;
import Models.Moves.MoveTypeCalculator;
import Models.Moves.MoveTypes;
import Models.Player.Player;

import java.util.List;

public class Pawn extends Piece{

    public static final MoveTypes[] myMoveTypes = {MoveTypes.PawnMove};
    public Pawn(boolean isWhite) {
        super(isWhite, Settings.PAWN_MOVE_LIMIT, myMoveTypes);
    }

    @Override
    public List<Move> canMove(BoardPosition startPosition, Board myBoard) {
        MoveTypeCalculator myMoveBrain = new MoveTypeCalculator(startPosition, myBoard);

        return myMoveBrain.calculateMoves();
    }
}
