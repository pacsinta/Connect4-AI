public class StudentPlayer extends Player {
    class Points {
        public static final int inf = Integer.MAX_VALUE;
        public static final int middle = 4;
        public static final int lineoftwo = 2;
        public static final int lineofthree = 5;
    }

    private static final int Depth = 3;

    int middleRow;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        middleRow = boardSize[1] / 2;

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
        for (int i = state[0].length; i >= 0; i--) {
            if (state[row][step] == player) {
                row = i;
                break;
            }
        }


        for (int x = -3; x < 1; x++) {
            for (int i = x; i < x + 4; i++) {
                // test rows
                boolean test_row = true;
                try {
                    if (test_row && (state[row][step + i] == player) && (i != 0)) {
                        value += Points.lineoftwo;
                    }
                } catch (IndexOutOfBoundsException e) {
                    test_row = false;
                }

                // test columns
                boolean test_col = true;
                try {
                    if (test_col && (state[row + i][step] == player) && (i != 0)) {
                        value += Points.lineoftwo;
                    }
                } catch (IndexOutOfBoundsException e) {
                    test_col = false;
                }

                // test diagonals
                boolean test_diag1 = true;
                try {
                    if (test_diag1 && (state[row + i][step - i] == player) && (i != 0)) {
                        value += Points.lineoftwo;
                    }
                } catch (IndexOutOfBoundsException e) {
                    test_diag1 = false;
                }

                boolean test_diag2 = true;
                try {
                    if (test_diag2 && (state[row - i][step + i] == player) && (i != 0)) {
                        value += Points.lineoftwo;
                    }
                } catch (IndexOutOfBoundsException e) {
                    test_diag2 = false;
                }
            }
        }


        return value;
    }

    class Node {
        Node[] nodes = new Node[boardSize[1]];
        int value;
        int step;
        Board board;

        Node(Board board, int node_depth) {
            this.board = new Board(board);
            this.node_depth = node_depth;
        }

        int node_depth;


        //TODO
        void printChildes(){
            System.out.println("Node depth: " + node_depth);
            for(int i = 0; i < boardSize[1]; i++) {
                if (nodes[i] != null) {
                    System.out.println("step: "+i+" value: "+nodes[i].value);
                }
            }
        }

        int calcValue(boolean maxValue) {
            createChildren();

            if (maxValue) {
                int max = -Points.inf;
                for (int i = 0; i < boardSize[1]; i++) {
                    if (nodes[i] != null) {
                        if (node_depth == 0) {
                            value = getValue(nodes[i].board, i, playerIndex);
                        } else {
                            value = nodes[i].calcValue(false);
                        }

                        if (value > max) {
                            max = value;
                            step = i;
                        }
                    }
                }

                printChildes();
                System.out.println("max value: " + max);
                return max;
            } else {
                int min = Points.inf;
                for (int i = 0; i < boardSize[1]; i++) {
                    if (nodes[i] != null) {
                        if (node_depth == 0) {
                            value = getValue(nodes[i].board, i, playerIndex);
                        } else {
                            value = nodes[i].calcValue(true);
                        }

                        if (value < min) {
                            min = value;
                            step = i;
                        }
                    }
                }

                printChildes();
                System.out.println("min value: " + min);
                return min;
            }
        }

        // generate nodes for all the possible moves
        void createChildren() {
            int nextPlayer = (playerIndex == 1) ? 2 : 1;

            for (int i = 0; i < boardSize[1]; i++) {
                Board copyBoard = new Board(board);
                if (copyBoard.stepIsValid(i)) {
                    copyBoard.step(nextPlayer, i);
                    nodes[i] = new Node(copyBoard, node_depth - 1);
                } else {
                    nodes[i] = null;
                }
            }
        }

    }

    @Override
    public int step(Board board) {
        //MINMAX algorithm
        int x = testing(board);


        Node root = new Node(board, 1);
        int v = root.calcValue(true);

        return root.step;
    }


    int testing(Board b){
        Board board = new Board(b);

        board.step(1, 0);
        board.step(2, 1);

        int test = getValue(board, 3, 1);

        return test;
    }
}
