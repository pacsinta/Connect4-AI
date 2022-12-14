public class StudentPlayer extends Player {
    class Points {
        public static final int inf = 100000;
        public static final int middle = 3;
        public static final int lineoftwo = 2;
        public static final int lineofthree = 6;
        public static final int enemy_middle = 0;
        public static final int enemy_lineoftwo = -1;
        public static final int enemy_lineofthree = -4;

    }

    final int RowCount = boardSize[0];
    final int ColumnCount = boardSize[1];

    int getOtherPlayer(int player) {
        return player == 1 ? 2 : 1;
    }

    private static final int Depth = 6;

    int middleColumn;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        middleColumn = boardSize[1] / 2;
    }


    private int[] getScoreInFour(int[] fourField, int player) {
        int playerCount = 0;
        int opponentCount = 0;
        for (int i = 0; i < nToConnect; i++) {
            if (fourField[i] == player) {
                playerCount++;
            } else if (fourField[i] != 0) {
                opponentCount++;
            }
        }

        return new int[]{playerCount, opponentCount};
    }

    private int convertToPoints(int playerCount, int opponentCount) {
        if (playerCount == 2 && opponentCount == 0) {
            return Points.lineoftwo;
        } else if (playerCount == 3 && opponentCount == 0) {
            return Points.lineofthree;
        } else if (playerCount == 0 && opponentCount == 2) {
            return Points.enemy_lineoftwo;
        } else if (playerCount == 0 && opponentCount == 3) {
            return Points.enemy_lineofthree;
        } else if (playerCount == 4) {
            return Points.inf;
        } else if (opponentCount == 4) {
            return -Points.inf;
        } else {
            return 0;
        }
    }

    private int getValue(Board board, int player) {
        int[][] state = board.getState();
        int value = 0;

        for (int i = 0; i < RowCount; i++) {
            if (state[i][middleColumn] == player) {
                value += Points.middle;
            } else if (state[i][middleColumn] == getOtherPlayer(player)) {
                value += Points.enemy_middle;
            }
        }

        for (int row = 0; row < RowCount; row++) {
            for (int col = 0; col < ColumnCount; col++) {
                if (row + 3 < RowCount && col - 3 >= 0) {
                    int[] score = getScoreInFour(new int[]{state[row][col], state[row + 1][col - 1], state[row + 2][col - 2], state[row + 3][col - 3]}, player);
                    value += convertToPoints(score[0], score[1]);
                    if (value >= Points.inf) {
                        return Points.inf;
                    } else if (value <= -Points.inf) {
                        return -Points.inf;
                    }
                }
                if (row + 3 < RowCount) {
                    int[] score = getScoreInFour(new int[]{state[row][col], state[row + 1][col], state[row + 2][col], state[row + 3][col]}, player);
                    value += convertToPoints(score[0], score[1]);
                    if (value >= Points.inf) {
                        return Points.inf;
                    } else if (value <= -Points.inf) {
                        return -Points.inf;
                    }
                }
                if (col + 3 < ColumnCount) {
                    int[] score = getScoreInFour(new int[]{state[row][col], state[row][col + 1], state[row][col + 2], state[row][col + 3]}, player);
                    value += convertToPoints(score[0], score[1]);
                    if (value >= Points.inf) {
                        return Points.inf;
                    } else if (value <= -Points.inf) {
                        return -Points.inf;
                    }
                }
                if (row + 3 < RowCount && col + 3 < ColumnCount) {
                    int[] score = getScoreInFour(new int[]{state[row][col], state[row + 1][col + 1], state[row + 2][col + 2], state[row + 3][col + 3]}, player);
                    value += convertToPoints(score[0], score[1]);
                    if (value >= Points.inf) {
                        return Points.inf;
                    } else if (value <= -Points.inf) {
                        return -Points.inf;
                    }
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
            for (int i = 0; i < ColumnCount; i++) {
                Board copyBoard = new Board(board);
                if (stepIsValid(i, copyBoard)) {
                    int nextPlayer = getOtherPlayer(node_player);
                    copyBoard.step(node_player, i);
                    nodes[i] = new Node(copyBoard, node_depth - 1, nextPlayer);
                } else {
                    nodes[i] = null;
                }
            }
        }

        void calcValue() {
            calcValue(true, -Points.inf, Points.inf);
        }

        void cleanMemory() {
            nodes = null;
        }

        int calcNotRoot(boolean maxValue, int alpha, int beta) {
            int minmax = maxValue ? -Points.inf : Points.inf;

            createChildren();

            for (int i = 0; i < ColumnCount; i++) {
                if (nodes[i] != null) {
                    value = nodes[i].calcValue(!maxValue, alpha, beta);

                    if (isMinMax(maxValue, value, minmax)) {
                        minmax = value;
                        step = i;
                    }

                    if (maxValue) {
                        alpha = Math.max(alpha, minmax);
                    } else {
                        beta = Math.min(beta, minmax);
                    }

                    if (maxValue && beta < minmax) {
                        break;
                    } else if (!maxValue && minmax < alpha) {
                        break;
                    }
                }
            }
            cleanMemory();
            return minmax;
        }

        int calcValue(boolean maxValue, int alpha, int beta) {
            if (node_depth == 0) {
                value = getValue(board, playerIndex);
                return value;
            } else if(board.gameEnded()){
                if(board.getWinner() == playerIndex){
                    return Points.inf;
                }else{
                    return -Points.inf;
                }
            }
            else {
                return calcNotRoot(maxValue, alpha, beta);
            }
        }
    }

    boolean stepIsValid(int step, Board board) {
        int top_cell_of_column = board.getState()[0][step];  //Bal felso sarok = 0, 0
        if (step < ColumnCount && top_cell_of_column == 0) {
            return true;
        }

        return false;
    }

    @Override
    public int step(Board board) {
        Node root = new Node(board, Depth, playerIndex);
        root.calcValue();

        if (stepIsValid(root.step, board)) {
            return root.step;
        } else {
            for (int i = 0; i < ColumnCount; i++) {
                if (stepIsValid(i, board)) {
                    return i;
                }
            }
        }

        return -1;
    }
}
