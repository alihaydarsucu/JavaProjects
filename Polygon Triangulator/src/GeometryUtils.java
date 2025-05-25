
/**
 * @author aliHaydarSucu 23050111019
 */
import java.util.List;

public class GeometryUtils {
    
    public static double calculateAngle(Point a, Point b, Point c) {
        // Calculate angle ABC (angle at point B)
        double ab_x = a.getX() - b.getX();
        double ab_y = a.getY() - b.getY();
        double cb_x = c.getX() - b.getX();
        double cb_y = c.getY() - b.getY();
        
        double dot = ab_x * cb_x + ab_y * cb_y;
        double cross = ab_x * cb_y - ab_y * cb_x;
        
        return Math.atan2(Math.abs(cross), dot);
    }
    
    public static boolean doLinesIntersect(Point a1, Point a2, Point b1, Point b2) {
        // Check if line segment a1-a2 intersects with line segment b1-b2
        int o1 = orientation(a1, a2, b1);
        int o2 = orientation(a1, a2, b2);
        int o3 = orientation(b1, b2, a1);
        int o4 = orientation(b1, b2, a2);
        
        // General case - different orientations
        if (o1 != o2 && o3 != o4) {
            return true;
        }
        
        // Special cases - collinear points
        // a1, a2 and b1 are collinear and b1 lies on segment a1a2
        if (o1 == 0 && onSegment(a1, b1, a2)) return true;
        
        // a1, a2 and b2 are collinear and b2 lies on segment a1a2
        if (o2 == 0 && onSegment(a1, b2, a2)) return true;
        
        // b1, b2 and a1 are collinear and a1 lies on segment b1b2
        if (o3 == 0 && onSegment(b1, a1, b2)) return true;
        
        // b1, b2 and a2 are collinear and a2 lies on segment b1b2
        if (o4 == 0 && onSegment(b1, a2, b2)) return true;
        
        return false;
    }
    
    public static boolean isPointInsidePolygon(Point point, Polygon polygon) {
        // Improved ray casting algorithm with better handling of edge cases
        List<Point> vertices = polygon.getPoints();
        int n = vertices.size();
        int intersections = 0;
        
        // Use a ray that goes to the right from the point
        double rayX = point.getX();
        double rayY = point.getY();
        
        for (int i = 0; i < n; i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % n);
            
            // Check if the ray intersects with edge p1-p2
            if (rayIntersectsEdge(rayX, rayY, p1, p2)) {
                intersections++;
            }
        }
        
        // Point is inside if odd number of intersections
        return intersections % 2 == 1;
    }
    
    /**
     * Helper method to check if a horizontal ray intersects with an edge
     */
    private static boolean rayIntersectsEdge(double rayX, double rayY, Point p1, Point p2) {
        double x1 = p1.getX(), y1 = p1.getY();
        double x2 = p2.getX(), y2 = p2.getY();
        
        // Check if ray is at the same height as the edge
        if (Math.abs(y1 - y2) < 1e-10) {
            return false; // Horizontal edge, no intersection
        }
        
        // Check if ray Y is between edge Y coordinates
        if (rayY < Math.min(y1, y2) || rayY > Math.max(y1, y2)) {
            return false;
        }
        
        // Calculate intersection X coordinate
        double intersectionX = x1 + (rayY - y1) * (x2 - x1) / (y2 - y1);
        
        // Ray intersects if intersection point is to the right of the test point
        return intersectionX > rayX;
    }
    
    public static boolean isPointInsideTriangle(Point p, Triangle triangle) {
        List<Point> vertices = triangle.getPoints();
        Point a = vertices.get(0);
        Point b = vertices.get(1);
        Point c = vertices.get(2);
        
        // Use barycentric coordinates
        double denominator = ((b.getY() - c.getY()) * (a.getX() - c.getX()) + 
                             (c.getX() - b.getX()) * (a.getY() - c.getY()));
        
        if (Math.abs(denominator) < 1e-10) {
            return false; // Degenerate triangle
        }
        
        double alpha = ((b.getY() - c.getY()) * (p.getX() - c.getX()) + 
                       (c.getX() - b.getX()) * (p.getY() - c.getY())) / denominator;
        
        double beta = ((c.getY() - a.getY()) * (p.getX() - c.getX()) + 
                      (a.getX() - c.getX()) * (p.getY() - c.getY())) / denominator;
        
        double gamma = 1 - alpha - beta;
        
        // Point is inside if all barycentric coordinates are positive
        return alpha > 1e-10 && beta > 1e-10 && gamma > 1e-10;
    }
    
    public static double calculateTriangleArea(Point a, Point b, Point c) {
        return Math.abs((b.getX() - a.getX()) * (c.getY() - a.getY()) - 
                       (c.getX() - a.getX()) * (b.getY() - a.getY())) / 2.0;
    }
    
    public static double distance(Point a, Point b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public static int orientation(Point p, Point q, Point r) {
        // Find orientation of ordered triplet (p, q, r)
        // Returns 0 if collinear, 1 if clockwise, 2 if counterclockwise
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - 
                     (q.getX() - p.getX()) * (r.getY() - q.getY());
        
        if (Math.abs(val) < 1e-10) return 0; // collinear
        return (val > 0) ? 1 : 2; // clockwise or counterclockwise
    }
    
    // Helper method to check if point q lies on segment pr
    private static boolean onSegment(Point p, Point q, Point r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && 
               q.getX() >= Math.min(p.getX(), r.getX()) &&
               q.getY() <= Math.max(p.getY(), r.getY()) && 
               q.getY() >= Math.min(p.getY(), r.getY());
    }
}