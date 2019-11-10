/* *****************************************************************************
 *  Name: Percolation
 *  Date: Nov. 19th, 2019
 *  Description: Percolation class and methods
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF mainUF;
    private final WeightedQuickUnionUF backwashUF;
    private boolean[] status;
    private final int size;
    private int num;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be larger than 0");
        size = n;
        status = new boolean[size * size + 2];
        for (int i = 1; i < size * size + 1; i++) {
            status[i] = false;
        }
        status[0] = true;
        status[size * size + 1] = true;
        num = 0;

        mainUF = new WeightedQuickUnionUF(size * size + 2);
        backwashUF = new WeightedQuickUnionUF(size * size + 1);

    }

    private void getUnion(WeightedQuickUnionUF tempUF, int i, int j) {
        if (!tempUF.connected(i, j)) {
            tempUF.union(i, j);
        }
    }

    private void checkBound(int i, int j) {
        if (i <= 0 || i > size) {
            throw new IllegalArgumentException("Row is out of bound");
        }
        if (j <= 0 || j > size) {
            throw new IllegalArgumentException("Column is out of bound");
        }
    }


    public void open(int row, int col) {
        checkBound(row, col);
        int position = (row - 1) * size + col;
        if (isOpen(row, col)) {
            return;
        }
        status[position] = true;
        num++;

        int lastRow = position - size;
        int nextRow = position + size;
        int lastCol = position - 1;
        int nextCol = position + 1;

        if (row == 1) {
            getUnion(mainUF, 0, position);
            getUnion(backwashUF, 0, position);
        }
        else if (status[lastRow]) {
            getUnion(mainUF, lastRow, position);
            getUnion(backwashUF, lastRow, position);
        }

        if (row == size) {
            getUnion(mainUF, position, size * size + 1);
        }
        else if (status[nextRow]) {
            getUnion(mainUF, nextRow, position);
            getUnion(backwashUF, nextRow, position);
        }

        if (col != 1 && status[lastCol]) {
            getUnion(mainUF, lastCol, position);
            getUnion(backwashUF, lastCol, position);
        }

        if (col != size && status[nextCol]) {
            getUnion(mainUF, nextCol, position);
            getUnion(backwashUF, nextCol, position);
        }
    }

    public boolean isOpen(int row, int col) {
    	checkBound(row, col);
        int position = (row - 1) * size + col;
        return status[position];
    }

    public boolean isFull(int row, int col) {
        checkBound(row, col);
        int position = (row - 1) * size + col;
        return (backwashUF.connected(0, position) && status[position]);

    }

    public int numberOfOpenSites() {
        return num;
    }

    public boolean percolates() {
        return mainUF.connected(0, size * size + 1);
    }
}
