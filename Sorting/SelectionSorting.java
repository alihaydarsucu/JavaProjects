package sorting;

import java.util.Arrays;
import java.util.Random;

public class SelectionSorting {

    public static void main(String[] args) {
        int[] numbers = new int[30];
        Random Random = new Random();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Random.nextInt(100);
        }

        System.out.println(Arrays.toString(numbers));

        for(int a = 0; a < numbers.length; a++){
            int minIndex1 = a;
            for(int b = a + 1; b < numbers.length; b++){
                if(numbers[b] < numbers[minIndex1]){
                    minIndex1 = b;
                }
            }
            int temporary = numbers[a];
            numbers[a] = numbers[minIndex1];
            numbers[minIndex1] = temporary;
        }
        
        System.out.println(Arrays.toString(numbers));
    }

}
