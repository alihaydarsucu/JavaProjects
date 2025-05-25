
/**
 * @author aliHaydarSucu 23050111019
 */
import java.util.ArrayList;
import java.util.List;

public class TriangulationEngine {
    
    /**
     * Custom recursive triangulation algorithm that avoids thin triangles
     * by selecting diagonals that maximize minimum edge ratios
     * @param polygon
     * @return 
     */
    public List<Triangle> triangulate(Polygon polygon) {
        List<Point> vertices = polygon.getPoints();
        
        // Base case: triangle
        if (vertices.size() == 3) {
            return List.of(new Triangle(vertices));
        }
        
        // Find the best diagonal to split the polygon
        DiagonalCandidate bestDiagonal = findBestDiagonal(polygon);
        
        if (bestDiagonal == null) {
            throw new IllegalStateException("Cannot find valid diagonal for polygon triangulation");
        }
        
        // Split polygon into two sub-polygons using the diagonal
        List<Polygon> subPolygons = splitPolygon(polygon, bestDiagonal);
        
        // Recursively triangulate both sub-polygons
        List<Triangle> result = new ArrayList<>();
        for (Polygon subPolygon : subPolygons) {
            result.addAll(triangulate(subPolygon));
        }
        
        return result;
    }
    
    /**
     * Finds the best diagonal that creates well-proportioned triangles
     * and avoids thin triangles by maximizing edge length ratios
     */
    private DiagonalCandidate findBestDiagonal(Polygon polygon) {
        List<Point> vertices = polygon.getPoints();
        int n = vertices.size();
        
        DiagonalCandidate bestCandidate = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        List<DiagonalCandidate> validCandidates = new ArrayList<>();
        
        // Try all possible diagonals
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                // Skip adjacent vertices and ensure we don't connect last to first
                if ((i == 0 && j == n - 1) || Math.abs(i - j) <= 1) {
                    continue;
                }
                
                Point p1 = vertices.get(i);
                Point p2 = vertices.get(j);
                
                // Check if this diagonal is valid (doesn't intersect polygon edges)
                if (isValidDiagonal(polygon, i, j)) {
                    double score = evaluateDiagonal(polygon, i, j);
                    DiagonalCandidate candidate = new DiagonalCandidate(i, j, p1, p2, score);
                    validCandidates.add(candidate);
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestCandidate = candidate;
                    }
                }
            }
        }
        
        // If no valid diagonals found, try a fallback approach
        if (bestCandidate == null) {
            bestCandidate = findFallbackDiagonal(polygon, validCandidates);
        }
        
        return bestCandidate;
    }
    
    /**
     * Evaluates a diagonal based on how well it avoids thin triangles
     * Higher score means better quality (avoids thin triangles)
     */
    private double evaluateDiagonal(Polygon polygon, int startIdx, int endIdx) {
        List<Point> vertices = polygon.getPoints();
        
        // Create the two sub-polygons that would result from this diagonal
        List<Polygon> subPolygons = createSubPolygons(polygon, startIdx, endIdx);
        
        double totalScore = 0;
        int triangleCount = 0;
        
        // Evaluate quality of all potential triangles in both sub-polygons
        for (Polygon subPoly : subPolygons) {
            if (subPoly.size() == 3) {
                // Direct triangle - evaluate its quality
                Triangle triangle = new Triangle(subPoly.getPoints());
                totalScore += evaluateTriangleQuality(triangle);
                triangleCount++;
            } else {
                // Estimate quality by checking edge length ratios
                totalScore += estimatePolygonTriangulationQuality(subPoly);
                triangleCount += (subPoly.size() - 2); // Expected number of triangles
            }
        }
        
        return triangleCount > 0 ? totalScore / triangleCount : 0;
    }
    
    /**
     * Evaluates triangle quality based on edge length ratios
     * Returns higher score for triangles with more balanced edge lengths
     */
    private double evaluateTriangleQuality(Triangle triangle) {
        List<Point> points = triangle.getPoints();
        
        // Calculate edge lengths
        double edge1 = GeometryUtils.distance(points.get(0), points.get(1));
        double edge2 = GeometryUtils.distance(points.get(1), points.get(2));
        double edge3 = GeometryUtils.distance(points.get(2), points.get(0));
        
        // Avoid degenerate triangles
        if (edge1 < 1e-10 || edge2 < 1e-10 || edge3 < 1e-10) {
            return Double.NEGATIVE_INFINITY;
        }
        
        // Calculate edge ratios to avoid thin triangles
        double maxEdge = Math.max(Math.max(edge1, edge2), edge3);
        double minEdge = Math.min(Math.min(edge1, edge2), edge3);
        double edgeRatio = minEdge / maxEdge; // Higher ratio = better quality
        
        // Also consider area to avoid very small triangles
        double area = triangle.calculateArea();
        double perimeter = edge1 + edge2 + edge3;
        double compactness = (4 * Math.PI * area) / (perimeter * perimeter); // Isoperimetric quotient
        
        // Combine metrics: prioritize good edge ratios and reasonable compactness
        return edgeRatio * 0.7 + compactness * 0.3;
    }
    
    /**
     * Estimates the quality of triangulation for a polygon
     * without actually triangulating it (for efficiency)
     */
    private double estimatePolygonTriangulationQuality(Polygon polygon) {
        List<Point> vertices = polygon.getPoints();
        double totalScore = 0;
        int samples = 0;
        
        // Sample a few potential triangles from the polygon
        for (int i = 0; i < vertices.size(); i++) {
            int next1 = (i + 1) % vertices.size();
            int next2 = (i + 2) % vertices.size();
            
            Triangle sampleTriangle = new Triangle(List.of(
                vertices.get(i), vertices.get(next1), vertices.get(next2)
            ));
            
            totalScore += evaluateTriangleQuality(sampleTriangle);
            samples++;
        }
        
        return samples > 0 ? totalScore / samples : 0;
    }
    
    /**
     * Checks if a diagonal between two vertices is valid
     * (doesn't intersect with polygon edges)
     */
    private boolean isValidDiagonal(Polygon polygon, int startIdx, int endIdx) {
        List<Point> vertices = polygon.getPoints();
        Point p1 = vertices.get(startIdx);
        Point p2 = vertices.get(endIdx);
        
        // Check if diagonal length is reasonable
        double diagonalLength = GeometryUtils.distance(p1, p2);
        if (diagonalLength < 1e-6) {
            return false;
        }
        
        // Check intersection with all polygon edges
        for (int i = 0; i < vertices.size(); i++) {
            int next = (i + 1) % vertices.size();
            
            // Skip edges that share endpoints with the diagonal
            if (i == startIdx || i == endIdx || next == startIdx || next == endIdx) {
                continue;
            }
            
            Point edgeStart = vertices.get(i);
            Point edgeEnd = vertices.get(next);
            
            if (GeometryUtils.doLinesIntersect(p1, p2, edgeStart, edgeEnd)) {
                return false;
            }
        }
        
        // Check if diagonal is inside the polygon using multiple test points
        Point midpoint = new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
        
        // Test multiple points along the diagonal
        for (double t = 0.2; t <= 0.8; t += 0.2) {
            Point testPoint = new Point(
                p1.getX() + t * (p2.getX() - p1.getX()),
                p1.getY() + t * (p2.getY() - p1.getY())
            );
            
            if (!GeometryUtils.isPointInsidePolygon(testPoint, polygon)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Fallback method when no valid diagonals are found using strict criteria
     * Uses relaxed validation and alternative approaches
     */
    private DiagonalCandidate findFallbackDiagonal(Polygon polygon, List<DiagonalCandidate> validCandidates) {
        List<Point> vertices = polygon.getPoints();
        int n = vertices.size();
        
        // If we have any valid candidates, return the best one
        if (!validCandidates.isEmpty()) {
            return validCandidates.stream()
                    .max((a, b) -> Double.compare(a.score, b.score))
                    .orElse(null);
        }
        
        // Try with relaxed intersection checking
        DiagonalCandidate bestCandidate = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                if ((i == 0 && j == n - 1) || Math.abs(i - j) <= 1) {
                    continue;
                }
                
                Point p1 = vertices.get(i);
                Point p2 = vertices.get(j);
                
                // Use relaxed validation
                if (isValidDiagonalRelaxed(polygon, i, j)) {
                    double score = evaluateDiagonal(polygon, i, j);
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestCandidate = new DiagonalCandidate(i, j, p1, p2, score);
                    }
                }
            }
        }
        
        // If still no valid diagonal, create a simple fan triangulation
        if (bestCandidate == null) {
            // Find a vertex that can form a triangle with two consecutive vertices
            for (int i = 0; i < n; i++) {
                int next1 = (i + 1) % n;
                int next2 = (i + 2) % n;
                
                // Check if we can form a triangle with consecutive vertices
                Triangle testTriangle = new Triangle(List.of(
                    vertices.get(i), vertices.get(next1), vertices.get(next2)
                ));
                
                if (testTriangle.calculateArea() > 1e-9) {
                    // This will create a triangle by connecting vertex i to i+2
                    return new DiagonalCandidate(i, next2, vertices.get(i), vertices.get(next2), 0.1);
                }
            }
        }
        
        return bestCandidate;
    }
    
    /**
     * Relaxed validation for diagonals when strict validation fails
     */
    private boolean isValidDiagonalRelaxed(Polygon polygon, int startIdx, int endIdx) {
        List<Point> vertices = polygon.getPoints();
        Point p1 = vertices.get(startIdx);
        Point p2 = vertices.get(endIdx);
        
        // Check if diagonal length is reasonable (not too small)
        double diagonalLength = GeometryUtils.distance(p1, p2);
        if (diagonalLength < 1e-6) {
            return false;
        }
        
        // Count intersections with polygon edges (allow some tolerance)
        int intersectionCount = 0;
        for (int i = 0; i < vertices.size(); i++) {
            int next = (i + 1) % vertices.size();
            
            // Skip edges that share endpoints with the diagonal
            if (i == startIdx || i == endIdx || next == startIdx || next == endIdx) {
                continue;
            }
            
            Point edgeStart = vertices.get(i);
            Point edgeEnd = vertices.get(next);
            
            if (GeometryUtils.doLinesIntersect(p1, p2, edgeStart, edgeEnd)) {
                intersectionCount++;
            }
        }
        
        // Allow diagonal if it has minimal intersections
        return intersectionCount <= 1;
    }
    
    /**
     * Splits a polygon into two sub-polygons using a diagonal
     */
    private List<Polygon> splitPolygon(Polygon polygon, DiagonalCandidate diagonal) {
        return createSubPolygons(polygon, diagonal.startIdx, diagonal.endIdx);
    }
    
    /**
     * Creates two sub-polygons from a diagonal
     */
    private List<Polygon> createSubPolygons(Polygon polygon, int startIdx, int endIdx) {
        List<Point> vertices = polygon.getPoints();
        List<Polygon> result = new ArrayList<>();
        
        // First sub-polygon: from startIdx to endIdx
        List<Point> poly1Points = new ArrayList<>();
        int current = startIdx;
        while (current != endIdx) {
            poly1Points.add(vertices.get(current));
            current = (current + 1) % vertices.size();
        }
        poly1Points.add(vertices.get(endIdx));
        
        // Second sub-polygon: from endIdx to startIdx
        List<Point> poly2Points = new ArrayList<>();
        current = endIdx;
        while (current != startIdx) {
            poly2Points.add(vertices.get(current));
            current = (current + 1) % vertices.size();
        }
        poly2Points.add(vertices.get(startIdx));
        
        // Only add valid polygons (at least 3 vertices)
        if (poly1Points.size() >= 3) {
            result.add(new Polygon(poly1Points));
        }
        if (poly2Points.size() >= 3) {
            result.add(new Polygon(poly2Points));
        }
        
        return result;
    }
    
    /**
     * Helper class to store diagonal information
     */
    private static class DiagonalCandidate {
        int startIdx, endIdx;
        Point start, end;
        double score;
        
        DiagonalCandidate(int startIdx, int endIdx, Point start, Point end, double score) {
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.start = start;
            this.end = end;
            this.score = score;
        }
    }
}