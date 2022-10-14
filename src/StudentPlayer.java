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

    class Node {
        Node[] nodes = new Node[boardSize[1]];
        int value;
        int step;
        Board board;

        Node(int value, Board board, int node_depth) {
            this.value = value;
            this.board = new Board(board);
            this.node_depth = node_depth;
            board.getLastPlayerColumn();
        }

        Node(Board board, int node_depth) {
            this.value = -1;
            this.board = new Board(board);
            this.node_depth = node_depth;
        }

        int node_depth;
        int calcValue(boolean maxValue) {
            if(node_depth != 0){
                createChildren();
            }

            if(maxValue){
                int max = -Points.inf;
                for (int i = 0; i < boardSize[1]; i++) {
                    if(nodes[i] != null){
                        if(node_depth == 0){
                            value = getValue(nodes[i].board, i, playerIndex);
                        }else{
                            value = nodes[i].calcValue(false);
                        }

                        if(value > max){
                            max = value;
                            step = i;
                        }
                    }
                }

                return max;
            }else{
                int min = Points.inf;
                for (int i = 0; i < boardSize[1]; i++) {
                    if(nodes[i] != null){
                        if(node_depth == 0){
                            value = getValue(nodes[i].board, i, playerIndex);
                        }else{
                            value = nodes[i].calcValue(true);
                        }

                        if(value < min){
                            min = value;
                            step = i;
                        }
                    }
                }

                return min;
            }
        }

        // generate nodes for all the possible moves
        void createChildren() {
            int nextPlayer = 1;
            if (board.getLastPlayerIndex() == 1) {
                nextPlayer = 2;

                for (int i = 0; i < boardSize[1]; i++) {
                    Board copyBoard = new Board(board);
                    if(copyBoard.stepIsValid(i)){
                        int moveValue = getValue(copyBoard, i, nextPlayer);

                        copyBoard.step(nextPlayer, i);
                        nodes[i] = new Node(moveValue, copyBoard, node_depth - 1);
                    }else{
                        nodes[i] = null;
                    }
                }
            }
        }
    }

    @Override
    public int step(Board board) {
        //MINMAX algorithm

        Node root = new Node(board, Depth);
        root.calcValue(true);

        return root.step;
    }
}
