package Models.Moves;

import java.util.ArrayList;
import java.util.List;

public class MoveEvaluation {

    public List<Move> moves;
    public int valuation;

    public MoveEvaluation(List<Move> moves, int valuation){
        this.moves = moves;
        this.valuation = valuation;
    }

    public MoveEvaluation clone(){
        MoveEvaluation clone = new MoveEvaluation(new ArrayList<>(moves), valuation);
        return clone;
    }
}
