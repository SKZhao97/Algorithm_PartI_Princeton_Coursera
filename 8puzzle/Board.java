/* *****************************************************************************
 *  Name: Board.class
 *  Date: Dec. 4th, 2019
 *  Description: Board
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    final private int[][] tiles;
    final private int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null || tiles.length == 0)
            throw new IllegalArgumentException();
        this.dimension = tiles.length;
        this.tiles = new int[dimension][];

        for (int i = 0; i < dimension; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], dimension);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                temp.append(String.format("%2d ", tiles[i][j]));
            }
            temp.append("\n");
        }
        return temp.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if ((tiles[i][j] != (dimension * i + j + 1)) && tiles[i][j] != 0) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != 0) {
                    int goalRow = (tiles[i][j] - 1) / dimension;
                    int goalCol = (tiles[i][j] - 1) % dimension;
                    manhattan += (Math.abs(goalRow - i) + Math.abs(goalCol - j));
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (y.getClass() != this.getClass()) return false;

        Board board = (Board) y;

        if (this.dimension != board.dimension) {
            return false;
        }
        else {
            // for (int i = 0; i < tiles.length; i++) {
            //    if (!Arrays.deepEquals(tiles[i], board.tiles[i])) {
            //        return false;
            //    }
            // }
            if (!Arrays.deepEquals(tiles, board.tiles)) {
                return false;
            }
            return true;
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();

        // find blank position
        int blankRow = 0;
        int blankCol = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break;
                }
            }
        }

        if (blankRow > 0) {
            int[][] up = copyOf(tiles);
            swap(up, blankRow, blankCol, blankRow - 1, blankCol);
            neighbors.push(new Board(up));
        }
        if (blankRow < dimension - 1) {
            int[][] down = copyOf(tiles);
            swap(down, blankRow, blankCol, blankRow + 1, blankCol);
            neighbors.push(new Board(down));
        }
        if (blankCol > 0) {
            int[][] left = copyOf(tiles);
            swap(left, blankRow, blankCol, blankRow, blankCol - 1);
            neighbors.push(new Board(left));
        }
        if (blankCol < dimension - 1) {
            int[][] right = copyOf(tiles);
            swap(right, blankRow, blankCol, blankRow, blankCol + 1);
            neighbors.push(new Board(right));
        }
        return neighbors;
    }

    private int[][] copyOf(int[][] origin) {
        int[][] copy = new int[origin.length][];
        for (int row = 0; row < origin.length; row++) {
            copy[row] = origin[row].clone();
        }
        return copy;
    }

    private void swap(int[][] v, int rowA, int colA, int rowB, int colB) {
        int swap = v[rowA][colA];
        v[rowA][colA] = v[rowB][colB];
        v[rowB][colB] = swap;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] temp = copyOf(tiles);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j + 1 < dimension) {
                    if (tiles[i][j] != 0 && tiles[i][j + 1] != 0) {
                        swap(temp, i, j, i, j + 1);
                        Board twinBoard = new Board(temp);
                        return twinBoard;
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[][] blocks = { { 0, 3, 6 }, { 1, 2, 8 }, { 5, 4, 7 } };
        //int[][] blocks = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board test = new Board(blocks);
        System.out.println(test.toString());
        for (Board ok : test.neighbors()) {
            System.out.println(ok.toString());
        }
        System.out.println(test.twin());
        System.out.println(test.hamming());
        System.out.println(test.manhattan());

        System.out.println(test.dimension);
    }
}
