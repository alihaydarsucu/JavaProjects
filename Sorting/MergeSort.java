package sorting;

import java.util.Random;
import java.util.Arrays;

public class MergeSort {

    public static void main(String[] args) {
        int[] numbers = new int[10];
        Random Random = new Random();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Random.nextInt(100);
        }
        System.out.println(Arrays.toString(numbers));

        mergeSorting(numbers);
        System.out.println(Arrays.toString(numbers));
    }

    public static void mergeSorting(int[] array) {
        
        if( array.length < 2){
            return;
        }
        int length = array.length;
        int midIndex = length / 2;
        int[] leftPart = new int[midIndex];
        int[] rightPart = new int[length - midIndex];
        
        for (int i = 0; i < midIndex; i++) {
            leftPart[i] = array[i];
        }
        for (int i = midIndex; i < length; i++) {
            rightPart[i - midIndex] = array[i];
        }

        mergeSorting(leftPart);
        mergeSorting(rightPart);

        merge(array, leftPart, rightPart);

    }

    public static void merge(int[] array, int[] leftPart, int[] rightPart) {
        int i = 0, j = 0, k = 0;
        while (i < leftPart.length && j < rightPart.length) {
            if (leftPart[i] <= rightPart[j]) {
                array[k] = leftPart[i];
                i++;
            } else {
                array[k] = rightPart[j];
                j++;
            }
            k++;

        }
        if( i < leftPart.length){
            while(i < leftPart.length){
                array[k] = leftPart[i];
                k++;
                i++;
            }
        }
        
        if( j < rightPart.length){
            while(j < rightPart.length){
                array[k] = rightPart[j];
                k++;
                j++;
            }
        }
    }
}
