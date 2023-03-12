package Models.PieceModels;

import Data.Settings;
import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Moves.Move;
import Models.Moves.MoveTypeCalculator;
import Models.Moves.MoveTypes;
import Models.Player.Player;

import java.util.List;

public class Queen extends Piece{

    public static final MoveTypes[] myMoveTypes = {MoveTypes.DiagonalMove, MoveTypes.StraightMove};
    public Queen(boolean isWhite) {
        super(isWhite, Settings.QUEEN_MOVE_LIMIT, myMoveTypes, 800);
    }

    @Override
    public List<Move> canMove(BoardPosition startPosition, Board myBoard) {
        MoveTypeCalculator myMoveBrain = new MoveTypeCalculator(startPosition, myBoard);

        return myMoveBrain.calculateMoves();
    }

    @Override
    public Piece clonePiece(){
        Piece myPiece = new Queen(isWhite);

        return myPiece;
    }
}
