public class Tester {
    static final int[] boardSize = new int[] {6, 7};

    public static void main(String[] args) {
        test1();
    }

    static void test1(){
        StudentPlayer players = new StudentPlayer(2, boardSize, 4);

        Board board = new Board(boardSize, 4);

        board.step(1, 3);
        board.step(2, 2);

        int[] results = {0, 0, 2, 6, 2, 2, 2};
        for (int i = 0; i < 7; i++) {
            int test = players.getValue(board, i, 1);
            if(test != results[i]){
                System.out.println("Test1:  i: "+i+" res: "+test+" wanted: "+results[i]);
            }
        }

        System.out.println("Test1: OK");
    }
}
