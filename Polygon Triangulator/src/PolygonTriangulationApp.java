
/**
 *
 * @author aliHaydarSucu 23050111019
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Main class for polygon triangulation homework Processes polygon files from
 * inputData folder and generates triangulated output images
 */
public class PolygonTriangulationApp {

    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 600;
    private static final int MARGIN = 50;

    public static void main(String[] args) {
        try {
            PolygonTriangulationApp app = new PolygonTriangulationApp();
            app.processAllPolygons();
        } catch (Exception e) {
            System.err.println("Error in triangulation application: " + e.getMessage());
        }
    }

    /**
     * Main processing method that handles all polygon files
     */
    public void processAllPolygons() {
        TriangulationEngine engine = new TriangulationEngine();

        // Create output directories if they don't exist
        createOutputDirectories();

        int successCount = 0;
        int failureCount = 0;

        System.out.println("+--------------------------------------------------------------+");
        System.out.println("|===         Polygon Triangulation Process Started          ===|");
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("| Processing polygons from inputData folder...                 |\n");

        // Process polygons 1.txt to 100.txt
        for (int i = 1; i <= 100; i++) {
            String inputFile = "inputData/" + i + ".txt";

            try {
                // Check if input file exists
                if (!new File(inputFile).exists()) {
                    System.out.println("File " + inputFile + " not found, skipping...");
                    continue;
                }

                // Load polygon from file
                Polygon polygon = loadPolygonFromFile(inputFile);

                // Perform triangulation
                long startTime = System.currentTimeMillis();
                List<Triangle> triangles = engine.triangulate(polygon);
                long endTime = System.currentTimeMillis();

                // Generate output images
                String polygonImageFile = "outputImages/" + i + "_polygon.png";
                String trianglesImageFile = "outputImages/" + i + "_triangles.png";

                generatePolygonImage(polygon, polygonImageFile);
                generateTrianglesImage(triangles, trianglesImageFile);

                System.out.println("  ✓ Successfully processed polygon " + i);
                System.out.println("  Images generated: " + polygonImageFile + ", " + trianglesImageFile + "\n");
                printPolygonTable(i, polygon, triangles, endTime - startTime);
                successCount++;

            } catch (IOException e) {
                System.err.println("  ✗ Failed to process polygon " + i + ": " + e.getMessage());

                // Provide more detailed error information
                try {
                    Polygon debugPolygon = loadPolygonFromFile(inputFile);
                    System.err.println("    Debug info:");
                    System.err.println("    - Vertices: " + debugPolygon.size());
                    System.err.println("    - Area: " + String.format("%.6f", debugPolygon.calculateArea()));
                    System.err.println("    - Is convex: " + debugPolygon.isConvex());

                    // Print vertex coordinates for debugging
                    System.err.println("    - Vertices:");
                    List<Point> points = debugPolygon.getPoints();
                    for (int j = 0; j < points.size(); j++) {
                        Point p = points.get(j);
                        System.err.println("      [" + j + "] ("
                                + String.format("%.3f", p.getX()) + ", "
                                + String.format("%.3f", p.getY()) + ")");
                    }
                } catch (IOException debugE) {
                    System.err.println("    Could not load polygon for debugging: " + debugE.getMessage());
                }

                failureCount++;
            }

            System.out.println();
        }

        // Print summary
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("|===                   Processing Summary                   ===|");
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("| Successfully processed: " + successCount + " polygons");
        System.out.println("| Failed to process: " + failureCount + " polygons");
        System.out.println("| Total attempts: " + (successCount + failureCount));
        System.out.println("| Success rate: " + String.format("%.1f%%",
                (successCount * 100.0) / Math.max(1, successCount + failureCount)));
        System.out.println("+--------------------------------------------------------------+");
    }

    /**
     * Loads a polygon from a text file Expected format: one vertex per line as
     * "x y"
     */
    private Polygon loadPolygonFromFile(String filename) throws IOException {
        List<Point> points = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                    continue;
                }

                try {
                    String[] coords = line.split("\\s+");
                    if (coords.length >= 2) {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        points.add(new Point(x, y));
                    } else {
                        System.err.println("Warning: Invalid line format at line " + lineNumber
                                + " in file " + filename + ": " + line);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Cannot parse coordinates at line " + lineNumber
                            + " in file " + filename + ": " + line);
                }
            }
        }

        if (points.size() < 3) {
            throw new IllegalArgumentException("Polygon must have at least 3 vertices. Found: " + points.size());
        }

        return new Polygon(points);
    }

    /**
     * Calculates triangle quality based on edge length ratios
     */
    private double calculateTriangleQuality(Triangle triangle) {
        List<Point> points = triangle.getPoints();

        double edge1 = GeometryUtils.distance(points.get(0), points.get(1));
        double edge2 = GeometryUtils.distance(points.get(1), points.get(2));
        double edge3 = GeometryUtils.distance(points.get(2), points.get(0));

        if (edge1 < 1e-10 || edge2 < 1e-10 || edge3 < 1e-10) {
            return 0; // Degenerate triangle
        }

        double maxEdge = Math.max(Math.max(edge1, edge2), edge3);
        double minEdge = Math.min(Math.min(edge1, edge2), edge3);

        return minEdge / maxEdge; // Higher ratio = better quality
    }

    /**
     * Creates necessary output directories
     */
    private void createOutputDirectories() {
        try {
            new File("outputImages").mkdirs();
            System.out.println("Output directory 'outputImages' is ready.");
        } catch (Exception e) {
            System.err.println("Warning: Could not create output directory: " + e.getMessage());
        }
    }

    /**
     * Generates PNG image showing the original polygon
     */
    private void generatePolygonImage(Polygon polygon, String filename) {
        try {
            BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Enable antialiasing for better quality
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Set background to white
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

            // Calculate bounds for scaling
            List<Point> points = polygon.getPoints();
            double[] bounds = calculateBounds(points);
            double minX = bounds[0], maxX = bounds[1], minY = bounds[2], maxY = bounds[3];

            // Convert polygon points to screen coordinates
            int[] xPoints = new int[points.size()];
            int[] yPoints = new int[points.size()];

            for (int i = 0; i < points.size(); i++) {
                Point p = points.get(i);
                xPoints[i] = (int) (MARGIN + (p.getX() - minX) / (maxX - minX) * (IMAGE_WIDTH - 2 * MARGIN));
                yPoints[i] = (int) (MARGIN + (maxY - p.getY()) / (maxY - minY) * (IMAGE_HEIGHT - 2 * MARGIN));
            }

            // Draw polygon outline
            g2d.setColor(new Color(0, 100, 200));
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.drawPolygon(xPoints, yPoints, points.size());

            // Fill polygon with light blue
            g2d.setColor(new Color(173, 216, 230, 100));
            g2d.fillPolygon(xPoints, yPoints, points.size());

            // Draw vertices
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(1.0f));
            for (int i = 0; i < points.size(); i++) {
                g2d.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);

                // Label vertices
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.valueOf(i), xPoints[i] + 6, yPoints[i] - 6);
                g2d.setColor(Color.RED);
            }

            // Add title
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Original Polygon (" + points.size() + " vertices)", 10, 25);

            g2d.dispose();
            ImageIO.write(image, "PNG", new File(filename));

        } catch (IOException e) {
            System.err.println("Failed to generate polygon image " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Generates PNG image showing the triangulated result
     */
    private void generateTrianglesImage(List<Triangle> triangles, String filename) {
        try {
            BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Enable antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Set background to white
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

            // Collect all points for bounds calculation
            List<Point> allPoints = new ArrayList<>();
            for (Triangle triangle : triangles) {
                allPoints.addAll(triangle.getPoints());
            }

            double[] bounds = calculateBounds(allPoints);
            double minX = bounds[0], maxX = bounds[1], minY = bounds[2], maxY = bounds[3];

            for (int t = 0; t < triangles.size(); t++) {
                Triangle triangle = triangles.get(t);
                List<Point> points = triangle.getPoints();

                // Generate a distinct color for each triangle
                Color fillColor = generateTriangleColor(t, triangles.size());

                // Convert points to screen coordinates
                int[] xPoints = new int[3];
                int[] yPoints = new int[3];

                for (int i = 0; i < 3; i++) {
                    Point p = points.get(i);
                    xPoints[i] = (int) (MARGIN + (p.getX() - minX) / (maxX - minX) * (IMAGE_WIDTH - 2 * MARGIN));
                    yPoints[i] = (int) (MARGIN + (maxY - p.getY()) / (maxY - minY) * (IMAGE_HEIGHT - 2 * MARGIN));
                }

                // Fill triangle
                g2d.setColor(fillColor);
                g2d.fillPolygon(xPoints, yPoints, 3);

                // Draw triangle outline
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawPolygon(xPoints, yPoints, 3);

                // Draw triangle number at centroid
                int centroidX = (xPoints[0] + xPoints[1] + xPoints[2]) / 3;
                int centroidY = (yPoints[0] + yPoints[1] + yPoints[2]) / 3;
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String triangleLabel = String.valueOf(t + 1);
                g2d.drawString(triangleLabel, centroidX - 5, centroidY + 3);
            }

            // Draw all vertices
            g2d.setColor(Color.RED);
            for (Point p : allPoints) {
                int x = (int) (MARGIN + (p.getX() - minX) / (maxX - minX) * (IMAGE_WIDTH - 2 * MARGIN));
                int y = (int) (MARGIN + (maxY - p.getY()) / (maxY - minY) * (IMAGE_HEIGHT - 2 * MARGIN));
                g2d.fillOval(x - 2, y - 2, 4, 4);
            }

            // Add title
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Triangulated Result (" + triangles.size() + " triangles)", 10, 25);

            g2d.dispose();
            ImageIO.write(image, "PNG", new File(filename));

        } catch (IOException e) {
            System.err.println("Failed to generate triangles image " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Calculates bounding box for a list of points
     */
    private double[] calculateBounds(List<Point> points) {
        double minX = points.stream().mapToDouble(Point::getX).min().orElse(0);
        double maxX = points.stream().mapToDouble(Point::getX).max().orElse(1);
        double minY = points.stream().mapToDouble(Point::getY).min().orElse(0);
        double maxY = points.stream().mapToDouble(Point::getY).max().orElse(1);

        // Add padding
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        minX -= rangeX * 0.1;
        maxX += rangeX * 0.1;
        minY -= rangeY * 0.1;
        maxY += rangeY * 0.1;

        return new double[]{minX, maxX, minY, maxY};
    }

    /**
     * Generates a distinct color for each triangle
     */
    private Color generateTriangleColor(int triangleIndex, int totalTriangles) {
        // Use HSB color space for better color distribution
        float hue = (float) triangleIndex / totalTriangles;
        float saturation = 0.7f + (triangleIndex % 3) * 0.1f; // Vary saturation slightly
        float brightness = 0.8f + (triangleIndex % 2) * 0.1f; // Vary brightness slightly

        Color baseColor = Color.getHSBColor(hue, saturation, brightness);

        // Add transparency for better visualization
        return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 150);
    }

    public void printPolygonTable(int index, Polygon polygon, List<Triangle> triangles, long triangulationTime) {
        int vertexCount = polygon.size();
        double area = polygon.calculateArea();
        boolean isConvex = polygon.isConvex();
        int triangleCount = triangles.size();

        double trianglesArea = triangles.stream().mapToDouble(Triangle::calculateArea).sum();
        double areaDifference = Math.abs(area - trianglesArea);
        double minTriangleArea = triangles.stream().mapToDouble(Triangle::calculateArea).min().orElse(0);
        double avgQuality = triangles.stream().mapToDouble(this::calculateTriangleQuality).average().orElse(0);
        int degenerateCount = (int) triangles.stream().filter(t -> t.calculateArea() < 1e-9).count();

        boolean valid = (triangleCount == vertexCount - 2) && (areaDifference < 1e-3) && (degenerateCount == 0);

        System.out.println("=== Polygon " + index + " ===");
        System.out.println("+--------------------------------+------------------------------+");
        System.out.printf("| %-30s | %-28s |\n", "Property", "Value");
        System.out.println("+--------------------------------+------------------------------+");
        System.out.printf("| %-30s | %-28d |\n", "Vertex Count", vertexCount);
        System.out.printf("| %-30s | %-28.6f |\n", "Area", area);
        System.out.printf("| %-30s | %-28s |\n", "Is Convex?", isConvex ? "✓ Convex" : "✗ Concave");
        System.out.printf("| %-30s | %-28d |\n", "Triangle Count", triangleCount);
        System.out.printf("| %-30s | %-28d |\n", "Time (ms)", triangulationTime);
        System.out.printf("| %-30s | %-28.6f |\n", "Triangles Area", trianglesArea);
        System.out.printf("| %-30s | %-28.6f |\n", "Area Difference", areaDifference);
        System.out.printf("| %-30s | %-28d |\n", "Degenerate Triangle Count", degenerateCount);
        System.out.printf("| %-30s | %-28.9f |\n", "Minimum Triangle Area", minTriangleArea);
        System.out.printf("| %-30s | %-28.3f |\n", "Average Triangle Quality", avgQuality);
        System.out.printf("| %-30s | %-28s |\n", "Is Valid?", valid ? "✓ Passed" : "✗ Failed");
        System.out.println("+--------------------------------+------------------------------+\n");
    }
}
