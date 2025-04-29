package sorting;

import java.util.Random;
import java.util.Arrays;

public class İterationSorting {

    public static void main(String[] args) {
        int[] numbers = new int[10];
        Random Random = new Random();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Random.nextInt(100);
        }
        System.out.println(Arrays.toString(numbers));

        for (int i = 1; i < numbers.length; i++) {
            int temp1 = numbers[i];
            int j = i - 1;
            while (j >= 0 && numbers[j] > temp1) {
                numbers[j+1] = numbers[j];
                numbers[j] = temp1;
                j--;
            }
        }

        System.out.println(Arrays.toString(numbers));
    }

}
