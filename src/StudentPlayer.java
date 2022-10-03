public class StudentPlayer extends Player{
    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    @Override
    public int step(Board board) {

        return 0;
    }

    private int getValue(Board board){

        //TODO
    }

    private int minValue(){
        //TODO
    }

    private int maxValue(Board board){
        if(board.gameEnded()) return getValue();

    }
}
