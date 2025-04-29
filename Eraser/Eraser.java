package eraser;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Eraser {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Give a text to remove the spaces from top:");
        StringBuilder text = new StringBuilder();

        // Get text input from the user (until an empty line)
        String line;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).equals("END")) {
            text.append(line).append("\n");
        }

        System.out.println("How many spaces should be deleted from the beginning of each line?");
        int spacesToRemove = 8; // Default value

        try {
            spacesToRemove = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, using default value of 8 spaces.");
        }

        String processed = removeLeadingSpaces(text.toString(), spacesToRemove);
        System.out.println("\nProcessed text:");
        System.out.println(processed);

        scanner.close();
    }

    /**
     * Method that deletes 8 (default, it can change) spaces from the beginning of each line If the line
     * starts with less than 8 spaces, deletes all existing spaces.
     *
     * @param text Text to be processed
     * @param maxSpaces
     * @return Text with spaces deleted
     */
    public static String removeLeadingSpaces(String text, int maxSpaces) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int spaceCount = 0;

            // Count the leading spaces in the line
            for (int j = 0; j < line.length() && j < maxSpaces; j++) {
                if (line.charAt(j) == ' ') {
                    spaceCount++;
                } else {
                    break;
                }
            }

            // Remove the leading spaces (up to maxSpaces)
            if (spaceCount > 0) {
                line = line.substring(Math.min(spaceCount, maxSpaces));
            }

            result.append(line);

            // Add a new line if it's not the last line
            if (i < lines.length - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}
