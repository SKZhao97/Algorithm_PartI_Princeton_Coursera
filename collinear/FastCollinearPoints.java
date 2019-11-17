/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private int lineCount;
    private Node last;

    private class Node {
        LineSegment val;
        Node prev;
    }

    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }

        int length = points.length;
        if (length < 4) return;

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

        for (int i = 0; i < length; i++) {
            Point currentPoint = test[i];
            int targetPointsNum = 0;
            Point[] targetPoints = new Point[length - 1];

            for (int j = 0; j < length; j++) {
                if (i != j) {
                    targetPoints[targetPointsNum] = test[j];
                    targetPointsNum += 1;
                }
            }

            Arrays.sort(targetPoints, currentPoint.slopeOrder());

            Point start = null;
            Point end = null;
            int currentCount = 0;
            for (int j = 0; j < (targetPointsNum - 1); j++) {
                if (currentPoint.slopeTo(targetPoints[j]) == currentPoint.slopeTo(
                        targetPoints[j + 1])) {
                    if (start == null) {
                        if (currentPoint.compareTo(targetPoints[j]) > 0) {
                            end = currentPoint;
                            start = targetPoints[j];
                        }
                        else {
                            end = targetPoints[j];
                            start = currentPoint;
                        }
                        currentCount += 2;
                    }

                    if (start.compareTo(targetPoints[j + 1]) > 0) {
                        start = targetPoints[j + 1];
                    }

                    if (end.compareTo(targetPoints[j + 1]) < 0) {
                        end = targetPoints[j + 1];
                    }

                    currentCount += 1;

                    if (j == (targetPointsNum - 2)) {
                        if (currentCount >= 4 && currentPoint.compareTo(start) == 0) {
                            addLine(start, end);
                        }
                        currentCount = 0;
                        start = null;
                        end = null;
                    }
                }
                else {
                    if (currentCount >= 4 && currentPoint.compareTo(start) == 0) {
                        addLine(start, end);
                    }
                    currentCount = 0;
                    start = null;
                    end = null;
                }
            }
        }
    }

    private void addLine(Point x, Point y) {
        if (last != null) {
            Node newNode = new Node();
            newNode.prev = last;
            newNode.val = new LineSegment(x, y);
            last = newNode;
        }
        else {
            last = new Node();
            last.val = new LineSegment(x, y);
        }
        lineCount++;
    }


    public int numberOfSegments() {
        return lineCount;
    }

    public LineSegment[] segments() {
        LineSegment[] collines = new LineSegment[lineCount];
        Node store = last;
        for (int k = 0; k < lineCount; k++) {
            collines[k] = store.val;
            store = store.prev;
        }
        return collines;
    }

    public static void main(String[] args) {
        // read the n points from a file
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
