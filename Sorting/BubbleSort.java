package sorting;

import java.util.Random;
import java.util.Arrays;

public class BubbleSort {

    public static void main(String[] args) {
        int[] numbers = new int[10];
        Random Random = new Random();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Random.nextInt(100);
        }
        System.out.println(Arrays.toString(numbers));
        
        boolean myFlag = true;
        while(myFlag){
            myFlag = false;
            for(int i = 0; i < numbers.length -1; i++){
                if(numbers[i]>numbers[i+1]){
                    myFlag = true;
                    int temporary = numbers[i];
                    numbers[i] = numbers[i+1];
                    numbers[i+1] = temporary;
                }
            }
        }
        
        System.out.println(Arrays.toString(numbers));
    }

}
