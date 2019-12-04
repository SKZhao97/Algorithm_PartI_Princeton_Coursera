/* *****************************************************************************
 *  Name: Solver
 *  Date: Dec. 4th, 2019
 *  Description: Slover
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Deque;
import java.util.LinkedList;

public class Solver {
    private boolean solvable;
    private SearchNode solution;

    private class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private final SearchNode prev;
        private final int moves;
        private final int manhatten;
        private final int priority;

        SearchNode(Board board, SearchNode prev, int moves) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhatten = board.manhattan();
            this.priority = manhatten + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPrev() {
            return prev;
        }

        public int getMoves() {
            return moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        solution = null;
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        minPQ.insert(new SearchNode(initial, null, 0));

        while (true) {
            SearchNode curNode = minPQ.delMin();
            Board curBoard = curNode.getBoard();

            if (curBoard.isGoal()) {
                solvable = true;
                solution = curNode;
                break;
            }

            if (curBoard.twin().isGoal()) {
                solvable = false;
                break;
            }

            int moves = curNode.getMoves();
            Board prevBoard = moves > 0 ? curNode.getPrev().getBoard() : null;

            for (Board nextBoard : curBoard.neighbors()) {
                if (nextBoard.equals(prevBoard)) {
                    continue;
                }
                minPQ.insert(new SearchNode(nextBoard, curNode, moves + 1));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        int moves;
        if (isSolvable()) {
            moves = solution.getMoves();
        }
        else {
            moves = -1;
        }
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Deque<Board> solutionQueue = new LinkedList<>();
        SearchNode node = solution;
        while (node != null) {
            solutionQueue.addFirst(node.getBoard());
            node = node.getPrev();
        }
        return solutionQueue;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
