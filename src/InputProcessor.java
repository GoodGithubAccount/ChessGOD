import Data.Settings;

public class InputProcessor {

    public InputProcessor(){

    }

    public int[][] processInput(String input){
        int[][] storedInput = new int[2][2];

        String[] fromTo = input.split("-");
        String[] fromXY = fromTo[0].split(",");
        String[] toXY = fromTo[1].split(",");

        try {
            int fromX = Integer.parseInt(fromXY[0]);
            int fromY = Integer.parseInt(fromXY[1]);

            int toX = Integer.parseInt(toXY[0]);
            int toY = Integer.parseInt(toXY[1]);

            storedInput[0][0] = fromX - 1;
            storedInput[0][1] = fromY - 1;

            storedInput[1][0] = toX - 1;
            storedInput[1][1] = toY - 1;

        }catch (NumberFormatException e){

        }

        return storedInput;
    }
}
