import java.util.Arrays;

public class StudentPlayer extends Player {
    class Points {
        public static final int inf = Integer.MAX_VALUE;
        public static final int middle = 4;
        public static final int lineoftwo = 2;
        public static final int lineofthree = 5;
    }

    private static final int Depth = 1;

    int middleRow;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        middleRow = boardSize[1] / 2;

    }

    public int getValue(Board board, int step, int player) {
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
        for (int i = 0; i < boardSize[0]; i++) {
            if (state[i][step] == player) {
                row = i;
                break;
            }
        }

        //Row
        int start = Math.max(step - 3, 0);
        int start2 = Math.max(step - 3, 0);
        int count = 0;
        for (int n = start; n < start + 4; n++) {
            if (n + 4 > boardSize[1]) break;
            for (int i = n; i < n + 4; i++) {
                if (state[row][i] != player && state[row][i] != 0) {
                    break;
                }
                if (state[row][i] == player) {
                    count++;
                }
            }

            if (count == 2) {
                value += Points.lineoftwo;
            } else if (count == 3) {
                value -= Points.lineoftwo;
                value += Points.lineofthree;
            } else if (count == 4) {
                return Points.inf;
            }

            count = 0;
        }

        //Column
        for (int n = start2; n < start2 + 4; n++) {
            if (n + 4 > boardSize[0]) break;
            for (int i = n; i < n + 4; i++) {
                if (state[i][step] != player && state[i][step] != 0) {
                    break;
                }
                if (state[i][step] == player) {
                    count++;
                }
            }

            if (count == 2) {
                value += Points.lineoftwo;
            } else if (count == 3) {
                value -= Points.lineoftwo;
                value += Points.lineofthree;
            } else if (count == 4) {
                return Points.inf;
            }

            count = 0;
        }

        for (int n1 = start, n2 = start2; n1 < start + 4 && n2 < start2 + 4; n1++, n2++) {
            if (n1 + 4 > boardSize[0] || n2 + 4 > boardSize[1]) break;
            for (int i = n1, j = n2; i < n1 + 4 && j < n2 + 4; i++, j++) {
                if (state[i][j] != player && state[i][j] != 0) {
                    break;
                }
                if (state[i][j] == player) {
                    count++;
                }
            }

            if (count == 2) {
                value += Points.lineoftwo;
            } else if (count == 3) {
                value -= Points.lineoftwo;
                value += Points.lineofthree;
            } else if (count == 4) {
                return Points.inf;
            }

            count = 0;
        }

        return value;
    }

    class Node {
        Node[] nodes = null;
        int value;
        int step;
        Board board;

        Node(Board board, int node_depth) {
            this.board = new Board(board);
            this.node_depth = node_depth;
        }

        int node_depth;


        //TODO
        void printChildes() {
            System.out.println("Node depth: " + node_depth);
            for (int i = 0; i < boardSize[1]; i++) {
                if (nodes[i] != null) {
                    System.out.println("step: " + i + " value: " + nodes[i].value);
                }
            }
        }

        int[] endValues = null;

        boolean isMinMax(boolean getMax, int value, int minmax) {
            if (getMax) {
                if (value > minmax) {
                    return true;
                }
            } else {
                if (value < minmax) {
                    return true;
                }
            }

            return false;
        }

        int calcValue() {
            return calcValue(true);
        }

        int calcValue(boolean maxValue) {
            createChildren();

            int minmax = maxValue ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            if (node_depth == 0) {
                for (int i = 0; i < boardSize[1]; i++) {
                    endValues[i] = getValue(board, i, playerIndex);
                    System.out.print(endValues[i]);
                    if (isMinMax(maxValue, endValues[i], minmax)) {
                        minmax = endValues[i];
                        step = i;
                    }
                }
            } else {
                for (int i = 0; i < boardSize[1]; i++) {
                    if (nodes[i] != null) {
                        value = nodes[i].calcValue(!maxValue);

                        if (isMinMax(maxValue, value, minmax)) {
                            minmax = value;
                            step = i;
                        }
                    }
                }
            }

            return minmax;
        }

        // generate nodes for all the possible moves
        void createChildren() {
            int nextPlayer = (playerIndex == 1) ? 2 : 1;

            for (int i = 0; i < boardSize[1]; i++) {
                Board copyBoard = new Board(board);
                if (stepIsValid(i, copyBoard)) {
                    copyBoard.step(nextPlayer, i);
                    nodes[i] = new Node(copyBoard, node_depth - 1);
                } else {
                    nodes[i] = null;
                }
            }
        }

    }

    boolean stepIsValid(int step, Board board) {
        int top_cell_of_column = board.getState()[boardSize[0] - 1][step];
        if (board.stepIsValid(step) && top_cell_of_column == 0) {
            return true;
        }

        return false;
    }

    @Override
    public int step(Board board) {
        Node root = new Node(board, 0);
        int v = root.calcValue();

        return root.step;
    }
}
