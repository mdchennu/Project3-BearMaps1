package bearmaps.utils.ps;

import java.util.Comparator;
import java.util.List;

public class KDTree implements PointSet {

    private KDTreeNode root;

    /* Constructs a KDTree using POINTS. You can assume POINTS contains at least one
       Point object. */
    public KDTree(List<Point> points) {
        this.root = null;
        for (Point p : points) {
            root = insert(p);
        }
    }

    /*
    You might find this insert helper method useful when constructing your KDTree!
    Think of what arguments you might want insert to take in. If you need
    inspiration, take a look at how we do BST insertion!
     */

    private KDTreeNode insert(Point p) {
        return insertHelper(p, root, true);
    }

    private KDTreeNode insertHelper(Point p, KDTreeNode n, boolean isLR) {
        if (n == null) {
            n = new KDTreeNode(p, null, null, isLR);
        } else if (Double.compare(p.getX(), n.point.getX()) < 0 && isLR) {
            n.left = insertHelper(p, n.left, false);
        } else if (Double.compare(p.getX(), n.point.getX()) >= 0 && isLR) {
            n.right = insertHelper(p, n.right, false);
        } else if (Double.compare(p.getY(), n.point.getY()) < 0 && !isLR) {
            n.left = insertHelper(p, n.left, true);
        } else if (Double.compare(p.getY(), n.point.getY()) >= 0 && !isLR) {
            n.right = insertHelper(p, n.right, true);
        }
        return n;
    }


    /* Returns the closest Point to the inputted X and Y coordinates. This method
       should run in O(log N) time on average, where N is the number of POINTS. */
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        if (goal == null || root == null) {
            return null;
        }
        return nearestHelper(root, goal, root).point;
    }

    private KDTreeNode nearestHelper(KDTreeNode n, Point goal, KDTreeNode best) {

        KDTreeNode temp = new KDTreeNode(goal, null, null, false);
        KDTreeNode goodSide = null;
        KDTreeNode badSide = null;

        if (n == null) {
            return best;
        } else if (Point.distance(n.point, goal) < Point.distance(best.point, goal)) {
            best = n;
        }

        if (n.compare(n, temp) >= 0) {
            goodSide = n.left();
            badSide = n.right();
        } else if (n.compare(n, temp) < 0) {
            goodSide = n.right();
            badSide = n.left();
        }

        best = nearestHelper(goodSide, goal, best);

        //if it has something useful then check bad side
        Double bestDistance = Point.distance(best.point, goal);
        Point badDest;

        if (!n.orientation) {
            badDest = new Point(goal.getX(), n.point.getY());
        } else {
            badDest = new Point(n.point.getX(), goal.getY());
        }

        Double otherDistance = Point.distance(badDest, goal);

        if (otherDistance < bestDistance) {
            best = nearestHelper(badSide, goal, best);
        }
        return best;

    }

    private class KDTreeNode implements Comparator<KDTreeNode> {

        private Point point;
        private KDTreeNode left;
        private KDTreeNode right;
        private boolean orientation; // if x comparison = true, if y comparison = false

        // If you want to add any more instance variables, put them here!

        KDTreeNode(Point p) {
            this.point = p;
        }

        KDTreeNode(Point p, KDTreeNode left, KDTreeNode right, boolean orientation) {
            this.point = p;
            this.left = left;
            this.right = right;
            this.orientation = orientation;

        }

        Point point() {
            return point;
        }

        KDTreeNode left() {
            return left;
        }

        KDTreeNode right() {
            return right;
        }

        boolean orientation() {
            return orientation;
        }


        public int compare(KDTreeNode one, KDTreeNode goal) {
            int value;
            if (one.orientation) {
                value = Double.compare(one.point.getX(), goal.point.getX());

            } else {
                value = Double.compare(one.point.getY(), goal.point.getY());
            }
            return value;
        }

        // If you want to add any more methods, put them here!

    }
}
