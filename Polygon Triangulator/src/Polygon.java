
/**
 * @author aliHaydarSucu 23050111019
 */
import java.util.List;
import java.util.Collections;

public class Polygon extends Geometry {

    public Polygon(List<Point> points) {
        super(points);
        verifyClockwiseOrder();
    }

    @Override
    public double calculateArea() {
        // Shoelace formula implementation
        double area = 0.0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            area += points.get(i).getX() * points.get(j).getY();
            area -= points.get(j).getX() * points.get(i).getY();
        }

        return Math.abs(area) / 2.0;
    }

    public double getMinimumAngle() {
        double minAngle = Double.MAX_VALUE;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            Point prev = points.get((i - 1 + n) % n);
            Point curr = points.get(i);
            Point next = points.get((i + 1) % n);

            double angle = GeometryUtils.calculateAngle(prev, curr, next);
            minAngle = Math.min(minAngle, angle);
        }

        return minAngle;
    }

    public boolean isConvex() {
        int n = points.size();
        if (n < 3) {
            return false;
        }

        boolean hasPositive = false;
        boolean hasNegative = false;

        for (int i = 0; i < n; i++) {
            Point prev = points.get((i - 1 + n) % n);
            Point curr = points.get(i);
            Point next = points.get((i + 1) % n);

            // Calculate cross product to determine turn direction
            double cross = (curr.getX() - prev.getX()) * (next.getY() - curr.getY())
                    - (curr.getY() - prev.getY()) * (next.getX() - curr.getX());

            if (cross > 0) {
                hasPositive = true;
            }
            if (cross < 0) {
                hasNegative = true;
            }

            // If we have both positive and negative turns, it's not convex
            if (hasPositive && hasNegative) {
                return false;
            }
        }

        return true;
    }

    private void verifyClockwiseOrder() {
        // Calculate the signed area to determine orientation
        double signedArea = 0.0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            signedArea += (points.get(j).getX() - points.get(i).getX())
                    * (points.get(j).getY() + points.get(i).getY());
        }

        // If signed area is positive, vertices are in counter-clockwise order
        // We need clockwise order for our algorithm, so reverse if necessary
        if (signedArea > 0) {
            Collections.reverse(points);
        }
    }
}
