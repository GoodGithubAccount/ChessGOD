import Data.Settings;
import Models.Board.Board;
import Models.Moves.Move;
import Models.Moves.MoveTypes;
import Models.Moves.PieceMover;
import Models.PieceModels.Queen;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardMovementTests {

    Board myBoard;
    int[][] queenMovement = {{5,5},{6,6},{4,4},{3,3}};

    @BeforeEach
    void setUp() {
        myBoard = new Board();
        myBoard.fillBoard();
    }

    @RepeatedTest(value=4)
    @DisplayName("Queen movement works")
    void testQueenMovement(RepetitionInfo repetitionInfo){
        int currentRepetition = repetitionInfo.getCurrentRepetition() - 1;

        int y = queenMovement[currentRepetition][0];
        int x = queenMovement[currentRepetition][1];

        myBoard.boardState[3][5].piece = myBoard.boardState[7][3].piece;
        myBoard.boardState[7][3].piece = null;
        Move myMove = new Move(MoveTypes.StraightMove, myBoard.boardState[3][5], myBoard.boardState[y][x], myBoard.boardState[y][x],myBoard.boardState[y][x], myBoard.boardState[y][x]);
        PieceMover.movePiece(myBoard,myMove);

        assertEquals(Queen.class, myBoard.boardState[y][x].piece.getClass(),
                "Queen movement works");
    }
}
