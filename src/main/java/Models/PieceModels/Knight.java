package Models.PieceModels;

import Data.Settings;
import Models.Board.Board;
import Models.Board.BoardPosition;
import Models.Moves.Move;
import Models.Moves.MoveTypeCalculator;
import Models.Moves.MoveTypes;
import Models.Player.Player;

import java.util.List;

public class Knight extends Piece{

    public static final MoveTypes[] myMoveTypes = {MoveTypes.KnightMove};
    public Knight(boolean isWhite) {
        super(isWhite, Settings.KNIGHT_MOVE_LIMIT, myMoveTypes, 250);
    }

    @Override
    public List<Move> canMove(BoardPosition startPosition, Board myBoard) {
        MoveTypeCalculator myMoveBrain = new MoveTypeCalculator(startPosition, myBoard);

        return myMoveBrain.calculateMoves();
    }

    @Override
    public Piece clonePiece(){
        Piece myPiece = new Knight(isWhite);

        if(this.hasMoved){
            myPiece.hasMoved = true;
        }

        return myPiece;
    }
}
