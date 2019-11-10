/* *****************************************************************************
 *  Name: Percolation
 *  Date: Nov. 19th, 2019
 *  Description: Percolation class and methods
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF UF;
    private WeightedQuickUnionUF UF_backwash;
    private boolean[] status;
    private int size;
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

        UF = new WeightedQuickUnionUF(size * size + 2);
        UF_backwash = new WeightedQuickUnionUF(size * size + 1);

    }

    public void open(int row, int col) {
        if (row <= 0 || row > size) {
            throw new IndexOutOfBoundsException("Row is out of bound");
        }
        if (col <= 0 || col > size) {
            throw new IndexOutOfBoundsException("Column is out of bound");
        }

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
            if (!UF.connected(0, position)) {
                UF.union(0, position);
            }
            if (!UF_backwash.connected(0, position)) {
                UF_backwash.union(0, position);
            }
        }
        else {
            if (status[lastRow]) {
                if (!UF.connected(lastRow, position)) {
                    UF.union(lastRow, position);
                }
                if (!UF_backwash.connected(lastRow, position)) {
                    UF_backwash.union(lastRow, position);
                }
            }
        }

        if (row == size) {
            if (!UF.connected(position, size * size + 1)) {
                UF.union(position, size * size + 1);
            }
        }
        else {
            if (status[nextRow]) {
                if (!UF.connected(nextRow, position)) {
                    UF.union(nextRow, position);
                }
                if (!UF_backwash.connected(nextRow, position)) {
                    UF_backwash.union(nextRow, position);
                }
            }
        }

        if (col != 1 && status[lastCol]) {
            if (!UF.connected(lastCol, position)) {
                UF.union(lastCol, position);
            }
            if (!UF_backwash.connected(lastCol, position)) {
                UF_backwash.union(lastCol, position);
            }
        }

        if (col != size && status[lastCol]) {
            if (!UF.connected(nextCol, position)) {
                UF.union(nextCol, position);
            }
            if (!UF_backwash.connected(nextCol, position)) {
                UF_backwash.union(nextCol, position);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        int position = (row - 1) * size + col;
        return status[position];
    }

    public boolean isFull(int row, int col) {
        if (row <= 0 || row > size) {
            throw new IndexOutOfBoundsException("Row is out of bound");
        }
        if (col <= 0 || col > size) {
            throw new IndexOutOfBoundsException("Column is out of bound");
        }
        int position = (row - 1) * size + col;
        return UF_backwash.connected(0, position);

    }

    public int numOfOpenSites() {
        return num;
    }

    public boolean percolates() {
        return UF.connected(0, size * size + 1);
    }

    public static void main(String[] args) {
    }

}
