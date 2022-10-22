public class StudentPlayer extends Player {
    class Points {
        public static final int inf = Integer.MAX_VALUE;
        public static final int middle = 4;
        public static final int lineoftwo = 2;
        public static final int lineofthree = 5;
    }

    final int RowCount = boardSize[0];
    final int ColumnCount = boardSize[1];

    class Player {
        public final int me = 1;
        public final int opponent = 2;
    }

    int getOtherPlayer(int player) {
        return player == 1 ? 2 : 1;
    }

    private static final int Depth = 1;

    int middleColumn;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        middleColumn = boardSize[1] / 2;

    }


    private int getScoreInFour(int[] fourField, int player) {
        int playerCount = 0;
        int opponentCount = 0;
        for (int i = 0; i < nToConnect; i++) {
            if (fourField[i] == getOtherPlayer(player)) {
                opponentCount++;
            }
            if (fourField[i] == player) {
                playerCount++;
            }
        }

        if (playerCount == 2 && opponentCount == 0) {
            return Points.lineoftwo;
        } else if (playerCount == 3 && opponentCount == 0) {
            return Points.lineofthree;
        } else if (playerCount == 0 && opponentCount == 2) {
            return -Points.lineoftwo;
        } else if (playerCount == 0 && opponentCount == 3) {
            return -Points.lineofthree;
        } else if(playerCount == 4){
            return Points.inf;
        } else if(opponentCount == 4){
            return -Points.inf;
        }else {
            return 0;
        }
    }


    private int getValue(Board board, int player) {
        int[][] state = board.getState();
        int value = 0;

        for(int i = 0; i < RowCount; i++) {
            if(state[i][middleColumn] == player) {
                value += Points.middle;
            }
        }


        for (int row = 0; row < RowCount; row++) {
            for (int col = 0; col < ColumnCount; col++) {
                if(col+3 < ColumnCount) {
                    value += getScoreInFour(new int[]{state[row][col], state[row][col + 1], state[row][col + 2], state[row][col + 3]}, player);
                }
                if(row+3 < RowCount) {
                    value += getScoreInFour(new int[]{state[row][col], state[row + 1][col], state[row + 2][col], state[row + 3][col]}, player);
                }
                if(row+3 < RowCount && col+3 < ColumnCount) {
                    value += getScoreInFour(new int[]{state[row][col], state[row + 1][col + 1], state[row + 2][col + 2], state[row + 3][col + 3]}, player);
                }
                if(row+3 < RowCount && col-3 >= 0) {
                    value += getScoreInFour(new int[]{state[row][col], state[row + 1][col - 1], state[row + 2][col - 2], state[row + 3][col - 3]}, player);
                }
            }
        }


        return value;
    }

    class Node {
        Node[] nodes = new Node[boardSize[1]];
        int value;
        int step;
        int node_depth;
        int node_player;
        Board board;

        Node(Board board, int node_depth, int node_player) {
            this.board = new Board(board);
            this.node_depth = node_depth;
            this.node_player = node_player;
        }

        int[] endValues = new int[boardSize[1]];

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

        // generate nodes for all the possible moves
        void createChildren() {
            for (int i = 0; i < boardSize[1]; i++) {
                Board copyBoard = new Board(board);
                if (stepIsValid(i, copyBoard)) {
                    int nextPlayer = getOtherPlayer(node_player);
                    copyBoard.step(getOtherPlayer(nextPlayer), i);
                    nodes[i] = new Node(copyBoard, node_depth - 1, nextPlayer);
                } else {
                    nodes[i] = null;
                }
            }
        }

        int calcValue() {
            return calcValue(true);
        }

        void cleanMemory(){
            nodes = null;
        }
        int calcValue(boolean maxValue) {
            int minmax = maxValue ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            if (node_depth == 0) {
                value = getValue(board, node_player);
                if(value != 0) {
                    value = value;
                }
                return value;
            } else {
                createChildren();
                for (int i = 0; i < boardSize[1]; i++) {
                    if (nodes[i] != null) {
                        value = nodes[i].calcValue(!maxValue);

                        if (isMinMax(maxValue, value, minmax)) {
                            minmax = value;
                            step = i;
                        }
                    }
                }
                cleanMemory();
            }

            return minmax;
        }
    }

    boolean stepIsValid(int step, Board board) {
        int top_cell_of_column = board.getState()[0][step];  //Bal felso sarok = 0, 0
        if (board.stepIsValid(step) && top_cell_of_column == 0) {
            return true;
        }

        return false;
    }

    @Override
    public int step(Board board) {
        Node root = new Node(board, 6, playerIndex);
        root.calcValue();

        return root.step;
    }
}
