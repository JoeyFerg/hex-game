/****************************************************************************
 *  Command: HexBoardStats N0 N1 T
 *
 *  This program takes the board sizes N0,N1 and game count T as a command-line
 *  arguments. Then, the program runs T games for each board size N where
 *  N0 <= N <= N1 and where each play randomly chooses an unset tile to set in
 *  order to estimate the probability that player 1 will win.
 ****************************************************************************/

public class HexBoardStats {
    private int N0;
    private int N1;
    private int T;
    private int[] numberOfWins;

    public HexBoardStats(int N0, int N1, int T) {
        this.N0 = N0;
        this.N1 = N1;
        this.T = T;
        if ( N0 <= 0 || N1 <= 0 || T <= 0 ){ throw new java.lang.IllegalArgumentException(); }
        numberOfWins = new int[N1 - N0 + 1];
        for (int N = N0; N <= N1; N++) {
            for (int j = 0; j < T; j++) {
                HexBoard board = new HexBoard(N);
                int p = 1;
                while (!board.hasPlayer1Won() && !board.hasPlayer2Won()) {
                    int row = StdRandom.uniform(N);
                    int col = StdRandom.uniform(N);
                    if (board.isSet(row, col)) {
                        board.setTile(row, col, p);
                        p = 1 + (p % 2);
                    }
                }
                if (board.hasPlayer1Won()) { numberOfWins[N - N0] += 1; }
            }
        }
    }

    private double getP1WinProbabilityEstimate(int N) {
        if (N < N0 || N > N1) { throw new java.lang.IndexOutOfBoundsException(); }
        return ((double) numberOfWins[N - N0]) / T;
    }

    public static void main(String[] args) {
        Stopwatch timer = new Stopwatch();
        HexBoardStats stats = new HexBoardStats(10, 10, 100);
        for (int i = 2; i <= 15; i++) {
            StdOut.println(i + " " + stats.getP1WinProbabilityEstimate(i));
        }
        StdOut.println(timer.elapsedTime());
    }
}
