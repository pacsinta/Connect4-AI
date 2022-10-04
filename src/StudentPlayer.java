import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentPlayer extends Player {
    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    private int getValue(Board board) {

        //TODO
    }

    private int minValue() {
        //TODO
    }

    private int maxValue(Board board) {
        if (board.gameEnded()) return getValue();

    }

    @Override
    public int step(Board board) {
        //MINMAX algorithm


        //Create the game graph
        int depth = 5;
        List<ArrayList<Board>> graph = new ArrayList<>();

        try {
            graph.add(nextStates(board));
        } catch (ExceptionReturn e) {
            //TODO: handle exception
        }

        int max = 0;  // best found score
        int row = 0;
        int col = 0;

        for (int i = 0; i < depth; i++) {
            for (Board b : graph.get(i)) {
                try {
                    ArrayList<Board> next = nextStates(b);
                    graph.add(next);
                } catch (ExceptionReturn e) {
                    //TODO: handle exception
                }

            }
        }

        Random rand = new Random();
        return rand.nextInt(boardSize[1]);
    }

    private int minmaxAlgorithm(Board board, int depth) {
        maxValue(board, depth);
    }

    private int getValue(Board board) {

    }

    private int maxValue(Board board, int depth) {
        if (board.gameEnded()) return -1;

        ArrayList<Board> nextStates = nextStates(board);
        for (Board b : nextStates) {
            return Math.max(v, minValue(b, depth - 1));
        }
    }

    private int minValue(Board board, int depth) {
        if (board.gameEnded()) return -2;

        ArrayList<Board> nextStates = nextStates(board);
        for (Board b : nextStates) {
            maxValue(b, depth - 1);
        }
    }


    private static class ExceptionReturn extends Exception {
        public Board board;

        ExceptionReturn(Board message) {
            this.board = message;
        }
    }

    private ArrayList<Board> nextStates(Board board) {
        ArrayList<Integer> validSteps = board.getValidSteps();
        ArrayList<Board> nextStates = new ArrayList<>();

        for (Integer validStep : validSteps) {
            Board newBoard = new Board(board);
            newBoard.step(playerIndex, validStep);
            nextStates.add(newBoard);
        }

        return nextStates;
    }
}
