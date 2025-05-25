
/**
 * @author aliHaydarSucu 23050111019
 */
import java.util.List;

public class Triangle extends Geometry {

    public Triangle(List<Point> points) {
        super(points);
        if (points.size() != 3) {
            throw new IllegalArgumentException("Triangle must have exactly 3 points");
        }
    }

    @Override
    public double calculateArea() {
        // Using cross product formula
        Point a = points.get(0);
        Point b = points.get(1);
        Point c = points.get(2);

        return Math.abs((b.getX() - a.getX()) * (c.getY() - a.getY())
                - (c.getX() - a.getX()) * (b.getY() - a.getY())) / 2.0;
    }

    public double getMinimumAngle() {
        Point a = points.get(0);
        Point b = points.get(1);
        Point c = points.get(2);

        double angle1 = GeometryUtils.calculateAngle(a, b, c);
        double angle2 = GeometryUtils.calculateAngle(b, c, a);
        double angle3 = GeometryUtils.calculateAngle(c, a, b);

        return Math.min(Math.min(angle1, angle2), angle3);
    }

    public boolean isDegenerate() {
        // Check if triangle is too thin (minimum angle < 10 degrees)
        double minAngle = getMinimumAngle();
        return minAngle < Math.toRadians(5);
    }

    public double getMaximumAngle() {
        Point a = points.get(0);
        Point b = points.get(1);
        Point c = points.get(2);

        double angle1 = GeometryUtils.calculateAngle(a, b, c);
        double angle2 = GeometryUtils.calculateAngle(b, c, a);
        double angle3 = GeometryUtils.calculateAngle(c, a, b);

        return Math.max(Math.max(angle1, angle2), angle3);
    }

    @Override
    public String toString() {
        return "Triangle[" + points.get(0) + ", " + points.get(1) + ", " + points.get(2) + "]";
    }
}
