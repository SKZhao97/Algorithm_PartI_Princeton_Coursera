/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int count;
    private Node line;

    private class Node {
        LineSegment val;
        Node last;
    }

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.IllegalArgumentException();
        int length = points.length;

        Point[] test = new Point[length];

        for (int i = 0; i < length; i++) {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException();
            for (int j = i + 1; j < length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
            test[i] = points[i];
        }

        Arrays.sort(test);
        for (int a = 0; a < length - 3; a++) {
            for (int b = a + 1; b < length - 2; b++) {
                for (int c = b + 1; c < length - 1; c++) {
                    for (int d = c + 1; d < length; d++) {
                        double slope1 = test[a].slopeTo(test[b]);
                        double slope2 = test[b].slopeTo(test[c]);
                        double slope3 = test[c].slopeTo(test[d]);
                        if (slope1 == slope2 && slope2 == slope3) {
                            if (line != null) {
                                Node nextLine = new Node();
                                nextLine.val = new LineSegment(test[a], test[d]);
                                nextLine.last = line;
                                line = nextLine;
                            }
                            else {
                                line = new Node();
                                line.val = new LineSegment(test[a], test[d]);
                            }
                            count += 1;
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return count;
    }

    public LineSegment[] segments() {
        LineSegment[] collines = new LineSegment[count];
        Node store = line;
        for (int k = 0; k < count; k++) {
            collines[k] = store.val;
            store = store.last;
        }
        return collines;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : points) {
            p.draw();
        }

        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        StdDraw.show();
    }
}

