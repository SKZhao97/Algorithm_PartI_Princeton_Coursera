/* *****************************************************************************
 *  Name: KdTree
 *  Date: Dec. 11th, 2019
 *  Description: KdTree Implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private final Point2D point;
        private RectHV rect;
        private Node leftOrBottom;
        private Node rightOrUp;
        private boolean isVertical;

        private Node(Point2D p) {       // Consturctor
            this.point = p;
            this.rect = null;
            this.leftOrBottom = null;
            this.rightOrUp = null;
        }
    }

    // construct an empty set of points
    public KdTree() {
        this.size = 0;
        this.root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (this.size == 0);
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (contains(p)) {
            return;
        }
        if (isEmpty()) {
            root = new Node(p);
            root.isVertical = true;
            root.rect = new RectHV(0, 0, 1, 1);
        }
        else {
            Node tempNode = root;
            Node parent = null;
            boolean isLeft = true;
            while (tempNode != null) {
                parent = tempNode;
                if (tempNode.isVertical) {
                    if (p.x() < tempNode.point.x()) {
                        tempNode = tempNode.leftOrBottom;
                        isLeft = true;
                    }
                    else {
                        tempNode = tempNode.rightOrUp;
                        isLeft = false;
                    }
                }
                else {
                    if (p.y() < tempNode.point.y()) {
                        tempNode = tempNode.leftOrBottom;
                        isLeft = true;
                    }
                    else {
                        tempNode = tempNode.rightOrUp;
                        isLeft = false;
                    }
                }
            }

            Node newNode = new Node(p);
            newNode.isVertical = !parent.isVertical;
            if (isLeft) {
                if (parent.isVertical) {
                    newNode.rect = new RectHV(parent.rect.xmin(),
                                              parent.rect.ymin(),
                                              parent.point.x(),
                                              parent.rect.ymax());
                }
                else {
                    newNode.rect = new RectHV(parent.rect.xmin(),
                                              parent.rect.ymin(),
                                              parent.rect.xmax(),
                                              parent.point.y());
                }
                parent.leftOrBottom = newNode;
            }
            else {
                if (parent.isVertical) {
                    newNode.rect = new RectHV(parent.point.x(),
                                              parent.rect.ymin(),
                                              parent.rect.xmax(),
                                              parent.rect.ymax());
                }
                else {
                    newNode.rect = new RectHV(parent.rect.xmin(),
                                              parent.point.y(),
                                              parent.rect.xmax(),
                                              parent.rect.ymax());
                }
                parent.rightOrUp = newNode;
            }
        }
        size++;
    }

    // does the set contain point p
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            return false;
        }

        Node node = root;
        while (node != null) {
            if (node.isVertical) {
                if (p.x() < node.point.x()) {
                    node = node.leftOrBottom;
                }
                else if (p.x() > node.point.x()) {
                    node = node.rightOrUp;
                }
                else {
                    if (p.y() == node.point.y())
                        return true;
                    else
                        node = node.rightOrUp;
                }
            }
            else {
                if (p.y() < node.point.y()) {
                    node = node.leftOrBottom;
                }
                else if (p.y() > node.point.y()) {
                    node = node.rightOrUp;
                }
                else {
                    if (p.x() == node.point.x())
                        return true;
                    else
                        node = node.rightOrUp;
                }
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty()) {
            return;
        }
        drawElements(root, 0.0, 0.0, 1.0, 1.0);
    }


    private void drawElements(Node n, double x0, double y0, double x1, double y1) {
        if (n.isVertical) {
            // drawRedLine(n.point.x(), y0, y1);
            drawRedLine(n.point.x(), n.rect.ymin(), n.rect.ymax());
        }
        else {
            // drawBlueLine(n.point.y(), x0, x1);
            drawBlueLine(n.point.y(), n.rect.xmin(), n.rect.xmax());
        }
        drawPoint(n.point);
        if (n.leftOrBottom != null) drawElements(n.leftOrBottom, x0, y0, n.point.x(), y1);
        if (n.rightOrUp != null) drawElements(n.rightOrUp, n.point.x(), y0, x1, y1);
    }

    private void drawPoint(Point2D p) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(p.x(), p.y());
    }

    private void drawRedLine(double x, double y0, double y1) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(x, y0, x, y1);
    }

    private void drawBlueLine(double y, double x0, double x1) {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        StdDraw.line(x0, y, x1, y);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Queue<Point2D> queue = new Queue<Point2D>();
        rangeFinder(root, rect, queue);
        return queue;
    }

    private void rangeFinder(Node n, RectHV rect, Queue<Point2D> queue) {
        if (n == null) {
            return;
        }

        if (n.rect.intersects(rect)) {
            if (rect.contains(n.point))
                queue.enqueue(n.point);
            if (n.leftOrBottom != null) rangeFinder(n.leftOrBottom, rect, queue);
            if (n.rightOrUp != null) rangeFinder(n.rightOrUp, rect, queue);
        }
        else {
            return;
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        return nearestFinder(root, p, null, 2.0);
    }

    private Point2D nearestFinder(Node n, Point2D p, Point2D minPoint, double minDistance) {
        if (n == null) {
            return minPoint;
        }

        Point2D res = minPoint;
        double dis = n.point.distanceSquaredTo(p);
        if (dis < minDistance) {
            minDistance = dis;
            res = n.point;
        }
        if (n.leftOrBottom != null && n.rightOrUp != null) {
            if (sameSide(n.leftOrBottom.point, p, n)) {
                res = nearestFinder(n.leftOrBottom, p, res, minDistance);
                minDistance = res.distanceSquaredTo(p);
                if (n.rightOrUp.rect.distanceSquaredTo(p) < minDistance)
                    res = nearestFinder(n.rightOrUp, p, res, minDistance);
            }
            else {
                res = nearestFinder(n.rightOrUp, p, res, minDistance);
                minDistance = res.distanceSquaredTo(p);
                if (n.leftOrBottom.rect.distanceSquaredTo(p) < minDistance)
                    res = nearestFinder(n.leftOrBottom, p, res, minDistance);
            }
        }
        else if (n.leftOrBottom == null) {
            res = nearestFinder(n.rightOrUp, p, res, minDistance);
        }
        else if (n.rightOrUp == null) {
            res = nearestFinder(n.leftOrBottom, p, res, minDistance);
        }
        return res;
    }

    private boolean sameSide(Point2D p1, Point2D p2, Node parent) {
        if (parent.isVertical) {
            double x = parent.point.x();
            return (p1.x() - x) * (p2.x() - x) >= 0;
        }
        else {
            double y = parent.point.y();
            return (p1.y() - y) * (p2.y() - y) >= 0;
        }
    }
}
