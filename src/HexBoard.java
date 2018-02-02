/****************************************************************************
 *  This class manages an N-by-N hex game board .
 ****************************************************************************/

public class HexBoard {
    private int N;
    private int unsetTiles;
    private int[] recentMove;
    private int[][] playerMatrix;
    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF winningUF;

    public HexBoard(int N) {
        this.N = N;
        this.unsetTiles = N * N;
        recentMove = new int[2];
        playerMatrix = new int[N][N];
        unionFind = new WeightedQuickUnionUF((N*N)+4);
        winningUF = new WeightedQuickUnionUF(N*N);
    }

    private int calcIndex(int row, int col) {
        return (N * col) + row;
    }

    public int getPlayer(int row, int col) {
        return this.playerMatrix[row][col];
    }

    public boolean isSet(int row, int col) {
        return this.playerMatrix[row][col] == 0;
    }

    public boolean isOnWinningPath(int row, int col) {
        int recentIndex = (N * recentMove[1]) + recentMove[0];
        int checker = (N * col) + row;

        return winningUF.connected(recentIndex, checker);
    }

    public void setTile(int row, int col, int player) {
        this.playerMatrix[row][col] = player;
        this.recentMove[0] = row;
        this.recentMove[1] = col;

        if (( row + 1 < N ) && ( playerMatrix[row][col] == playerMatrix[row + 1][col] )) {
            unionFind.union(calcIndex(row, col), calcIndex(row + 1, col));
            winningUF.union(calcIndex(row, col), calcIndex(row + 1, col));
        }
        if (( col + 1 < N ) && ( playerMatrix[row][col] == playerMatrix[row][col + 1] )) {
            unionFind.union(calcIndex(row, col), calcIndex(row, col + 1));
            winningUF.union(calcIndex(row, col), calcIndex(row, col + 1));
        }
        if (( row - 1 >= 0 ) && ( playerMatrix[row][col] == playerMatrix[row - 1][col] )) {
            unionFind.union(calcIndex(row, col), calcIndex(row - 1, col));
            winningUF.union(calcIndex(row, col), calcIndex(row - 1, col));
        }
        if (( col - 1 >= 0 ) && (playerMatrix[row][col] == playerMatrix[row][col - 1] )) {
            unionFind.union(calcIndex(row, col), calcIndex(row, col - 1));
            winningUF.union(calcIndex(row, col), calcIndex(row, col - 1));
        }
        if (( row + 1 < N ) && ( col - 1 >= 0 ) && ( playerMatrix[row][col] == playerMatrix[row + 1][col - 1] )) {
            unionFind.union(calcIndex(row, col), calcIndex(row + 1, col - 1));
            winningUF.union(calcIndex(row, col), calcIndex(row + 1, col - 1));
        }
        if (( col + 1 < N ) && ( row - 1 >= 0 ) && ( playerMatrix[row][col] == playerMatrix[row - 1][col + 1] )) {
            unionFind.union(calcIndex(row, col), calcIndex(row - 1, col + 1));
            winningUF.union(calcIndex(row, col), calcIndex(row - 1, col + 1));
        }
        if ( player == 1 ) {
            if ( col == 0 )    { unionFind.union(calcIndex(row, col), N*N); }
            if ( col == N - 1) { unionFind.union(calcIndex(row, col), (N*N) + 1); }
        }
        if ( player == 2 ) {
            if ( row == 0 )    { unionFind.union(calcIndex(row, col), (N*N) + 3); }
            if ( row == N - 1) { unionFind.union(calcIndex(row, col), (N*N) + 2); }
        }
        unsetTiles--;
    }

    public boolean hasPlayer1Won() {
        return unionFind.connected((N*N), (N*N)+1);
    }

    public boolean hasPlayer2Won() {
        return unionFind.connected((N*N)+2, (N*N)+3);
    }

    public int numberOfUnsetTiles() {
        return unsetTiles;
    }
}