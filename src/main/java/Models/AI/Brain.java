package Models.AI;

import Models.Board.Board;
import Models.Board.PositionChecker;
import Models.Moves.Move;
import Models.Moves.MoveEvaluation;
import Models.Moves.PieceMover;
import Models.PieceModels.King;
import Models.PieceModels.Piece;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static Models.AI.Brain.evaluateBoard;

public class Brain {

    int depth;
    boolean isGood;
    Move savMove = null;

    int numberOfThreads = 4; // Adjust this value based on your requirements
    ForkJoinPool customForkJoinPool = new ForkJoinPool(numberOfThreads);

    public Brain(int depth) {
        this.depth = 9;
    }

    public static int evaluateBoard(Board myBoard, boolean isMax) {
        int score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = myBoard.boardState[i][j].piece;

                if (piece != null) {
                    int pieceValue = piece.pieceValue;
                    if (piece.isWhite == isMax) {
                        score += pieceValue;
                    } else {
                        score -= pieceValue;
                    }
                }
            }
        }

        return score;
    }

    public int minimaxAlphaBeta(Board myBoard, int depth, boolean isWhite, int alpha, int beta) {
        if (depth == this.depth) {
            int eval = evaluateBoard(myBoard, isGood);
            return eval;
        }
        MinimaxTask task = new MinimaxTask(myBoard, depth, isWhite, alpha, beta, this.depth, isGood);
        customForkJoinPool.commonPool().invoke(task);

        if (isWhite == isGood) {
            savMove = task.getBestMove();
            return task.getBestScore();
        } else {
            return task.getBestScore();
        }
    }

    public void recommendMove(boolean isWhite, Board myBoard) {
        savMove = null;
        isGood = isWhite;

        int bestScore = minimaxAlphaBeta(myBoard.cloneBoard(), 0, isWhite, Integer.MIN_VALUE, Integer.MAX_VALUE);

        System.out.println("BEST EVAL: " + bestScore);

        Move move = savMove;

        System.out.println("MOVE : " + (move.movePieceStartPosition.x + 1) + "," + (move.movePieceStartPosition.y + 1) + "-"
                + (move.moveTriggerPosition.x + 1) + "," + (move.moveTriggerPosition.y + 1));

        customForkJoinPool.shutdown();
    }

    static class MinimaxTask extends RecursiveTask<Integer> {
        private final Board board;
        private final int depth;
        private final boolean isWhite;
        private final int alpha;
        private final int beta;
        private final int maxDepth;
        private final boolean isGood;
        private int bestScore;
        private Move bestMove;

        MinimaxTask(Board board, int depth, boolean isWhite, int alpha, int beta, int maxDepth, boolean isGood) {
            this.board = board;
            this.depth = depth;
            this.isWhite = isWhite;
            this.alpha = alpha;
            this.beta = beta;
            this.maxDepth = maxDepth;
            this.isGood = isGood;
        }

        public Move getBestMove() {
            return bestMove;
        }

        public int getBestScore() {
            return bestScore;
        }

        @Override
        protected Integer compute() {
            if (depth == maxDepth) {
                return evaluateBoard(board, isGood);
            }

            List<Move> possibleMoves = PositionChecker.getMoves(!isWhite, board);

            if (isWhite == isGood) {
                bestScore = Integer.MIN_VALUE;
                int alpha = this.alpha;

                for (Move move : possibleMoves) {
                    Board tempBoard = board.cloneBoard();
                    Move tempMove = move.cloneMove(tempBoard);
                    PieceMover.movePiece(tempBoard, tempMove);

                    MinimaxTask task = new MinimaxTask(tempBoard, depth + 1, !isWhite, alpha, beta, maxDepth, isGood);
                    task.fork();

                    int score = task.join();

                    if (score > bestScore) {
                        bestScore = score;
                        alpha = Math.max(alpha, bestScore);
                        if (depth == 0) {
                            bestMove = move;
                        }
                    }

                    if (beta <= alpha) {
                        break;
                    }
                }

                return bestScore;
            } else {
                bestScore = Integer.MAX_VALUE;
                int beta = this.beta;

                for (Move move : possibleMoves) {
                    Board tempBoard = board.cloneBoard();
                    Move tempMove = move.cloneMove(tempBoard);
                    PieceMover.movePiece(tempBoard, tempMove);

                    MinimaxTask task = new MinimaxTask(tempBoard, depth + 1, !isWhite, alpha, beta, maxDepth, isGood);
                    task.fork();

                    int score = task.join();

                    if (score < bestScore) {
                        bestScore = score;
                        beta = Math.min(beta, bestScore);
                    }

                    if (beta <= alpha) {
                        break;
                    }
                }
                return bestScore;
            }
        }
    }
}
