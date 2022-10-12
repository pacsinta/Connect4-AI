import java.util.*;

public class StudentPlayer extends Player {
    class Points {
        public static final int inf = Integer.MAX_VALUE;
        public static final int middle = 4;
        public static final int lineoftwo = 2;
        public static final int lineofthree = 5;
    }

    private static final int Depth = 10;

    int middleRow;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        middleRow = boardSize[1] / 2;
        if (boardSize[1] % 2 == 1) {
            middleRow++;
        }
    }

    private int getValue(Board board, int step, int player) {
        int value = 0;
        Board copyBoard = new Board(board);
        copyBoard.step(player, step);

        // win
        if (copyBoard.gameEnded()) {
            return Points.inf;
        }

        // middle points
        if (step == middleRow) {
            value += Points.middle;
        }

        // lines of two/three points
        // first get the row
        int[][] state = copyBoard.getState();
        int row = 0;
        for (int i = 6; i >= 0; i--) {
            if (state[i][step] == player) {
                row = i;
                break;
            }
        }


        for (int x = -3; x < 1; x++) {
            for (int i = x; i < x + 4; i++) {
                // test rows
                boolean testrow = true;
                try{
                    if (testrow && (state[row][step + i] == player) && (i != 0)) {
                        value += Points.lineoftwo;
                    }
                }catch (IndexOutOfBoundsException e){
                    testrow = false;
                }

                // test columns
                boolean testcol = true;
                try{
                    if (testcol && (state[row + i][step] == player) && (i != 0)){
                        value += Points.lineoftwo;
                    }
                }catch (IndexOutOfBoundsException e){
                    testcol = false;
                }

                // test diagonals
                boolean testdiag1 = true;
                try{
                    if (testdiag1 && (state[row + i][step - i] == player) && (i != 0)){
                        value += Points.lineoftwo;
                    }
                }catch (IndexOutOfBoundsException e){
                    testdiag1 = false;
                }

                boolean testdiag2 = true;
                try{
                    if (testdiag2 && (state[row - i][step + i] == player) && (i != 0)){
                        value += Points.lineoftwo;
                    }
                }catch (IndexOutOfBoundsException e){
                    testdiag2 = false;
                }
            }
        }


        return value;
    }

    private int minValue(Board board) {
        int min = Points.inf;
        int step = 0;
        for (int i = 0; i < boardSize[1]; i++) {
            int value = getValue(board, i, this.playerIndex);
            if(value < min){
                min = value;
                step = i;
            }
        }

        return step;
    }

    private int maxValue(Board board) {
        int max = -Points.inf;
        int step = 0;
        for (int i = 0; i < boardSize[1]; i++) {
            int value = getValue(board, i, this.playerIndex);
            if(value > max){
                max = value;
                step = i;
            }
        }

        return step;
    }

    @Override
    public int step(Board board) {
        //MINMAX algorithm

        //create the graph of the game
        int[][] valueGraph = new int[boardSize[1]][];
        for(int i = 0; i < Depth; i++){

        }

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
