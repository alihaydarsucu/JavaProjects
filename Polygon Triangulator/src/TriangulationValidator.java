
/**
 * @author aliHaydarSucu 23050111019
 */
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TriangulationValidator {

    public static boolean validateTriangulation(Polygon original, List<Triangle> triangles) {
        return validateAreaEquality(original, triangles)
                && validateNoIntersections(triangles)
                && validateAllPointsUsed(original, triangles)
                && validateTriangleCount(original, triangles);
    }

    private static boolean validateAreaEquality(Polygon original, List<Triangle> triangles) {
        double originalArea = original.calculateArea();
        double totalTriangleArea = triangles.stream()
                .mapToDouble(Triangle::calculateArea)
                .sum();

        return Math.abs(originalArea - totalTriangleArea) < 1e-3;
    }

    private static boolean validateNoIntersections(List<Triangle> triangles) {
        // Check that triangle edges don't intersect with each other
        // (except at shared vertices)
        for (int i = 0; i < triangles.size(); i++) {
            for (int j = i + 1; j < triangles.size(); j++) {
                if (trianglesIntersectImproperly(triangles.get(i), triangles.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean trianglesIntersectImproperly(Triangle t1, Triangle t2) {
        List<Point> points1 = t1.getPoints();
        List<Point> points2 = t2.getPoints();

        // Check each edge of triangle 1 against each edge of triangle 2
        for (int i = 0; i < 3; i++) {
            Point a1 = points1.get(i);
            Point a2 = points1.get((i + 1) % 3);

            for (int j = 0; j < 3; j++) {
                Point b1 = points2.get(j);
                Point b2 = points2.get((j + 1) % 3);

                // Skip if edges share a vertex (proper sharing)
                if (a1.equals(b1) || a1.equals(b2) || a2.equals(b1) || a2.equals(b2)) {
                    continue;
                }

                // Check if edges intersect improperly
                if (GeometryUtils.doLinesIntersect(a1, a2, b1, b2)) {
                    return true; // Improper intersection found
                }
            }
        }
        return false;
    }

    private static boolean validateAllPointsUsed(Polygon original, List<Triangle> triangles) {
        Set<Point> originalPoints = new HashSet<>(original.getPoints());
        Set<Point> trianglePoints = new HashSet<>();

        for (Triangle triangle : triangles) {
            trianglePoints.addAll(triangle.getPoints());
        }

        return originalPoints.equals(trianglePoints);
    }

    private static boolean validateTriangleCount(Polygon original, List<Triangle> triangles) {
        // For a simple polygon with n vertices, triangulation should produce n-2 triangles
        int expectedTriangles = original.size() - 2;
        return triangles.size() == expectedTriangles;
    }

    public static void printValidationResults(Polygon original, List<Triangle> triangles) {
        System.out.println("=== Triangulation Validation ===");
        System.out.println("Original polygon area: " + String.format("%.6f", original.calculateArea()));

        double totalArea = triangles.stream().mapToDouble(Triangle::calculateArea).sum();
        System.out.println("Total triangles area: " + String.format("%.6f", totalArea));
        System.out.println("Area difference: " + String.format("%.6f", Math.abs(original.calculateArea() - totalArea)));

        System.out.println("Number of triangles: " + triangles.size());
        System.out.println("Expected triangles: " + (original.size() - 2));

        boolean isValid = validateTriangulation(original, triangles);
        System.out.println("Triangulation valid: " + isValid);

        // Detailed validation results
        System.out.println("Area equality: " + validateAreaEquality(original, triangles));
        System.out.println("No intersections: " + validateNoIntersections(triangles));
        System.out.println("All points used: " + validateAllPointsUsed(original, triangles));
        System.out.println("Correct triangle count: " + validateTriangleCount(original, triangles));

        System.out.println("----------------------------------------");
    }
}
