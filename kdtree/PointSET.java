/* *****************************************************************************
 *  Name: PointSET
 *  Date: Dec. 11th, 2019
 *  Description: PointSET
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private final SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        this.set = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!set.contains(p)) {
            set.add(p);
        }
    }

    // does the set contain point p
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<Point2D> pointQueue = new Queue<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point))
                pointQueue.enqueue(point);
        }
        return pointQueue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (set == null) {
            return null;
        }
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearest = p;
        for (Point2D q : set) {
            double distance = p.distanceSquaredTo(q);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = q;
            }
        }
        return nearest;
    }
}
