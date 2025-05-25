# Sorting Algorithms in Java

This folder demonstrates four fundamental sorting algorithms implemented in Java: Bubble Sort, Insertion Sort, Merge Sort, and Selection Sort. Each algorithm has its own Java class with a complete implementation and example usage.

## Table of Contents

1. [Algorithms Overview](#algorithms-overview)
2. [Implementation Details](#implementation-details)
3. [Usage Examples](#usage-examples)
4. [Performance Characteristics](#performance-characteristics)
5. [Visual Comparison](#visual-comparison)
6. [How to Run](#how-to-run)

## Algorithms Overview

| Algorithm          | Best Case  | Average Case | Worst Case | Space Complexity | Stable |
| ------------------ | ---------- | ------------ | ---------- | ---------------- | ------ |
| **Bubble Sort**    | O(n)       | O(n²)        | O(n²)      | O(1)             | Yes    |
| **Insertion Sort** | O(n)       | O(n²)        | O(n²)      | O(1)             | Yes    |
| **Merge Sort**     | O(n log n) | O(n log n)   | O(n log n) | O(n)             | Yes    |
| **Selection Sort** | O(n²)      | O(n²)        | O(n²)      | O(1)             | No     |

## Implementation Details

### Bubble Sort (`BubbleSort.java`)

- Repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order
- The pass through the list is repeated until the list is sorted
- Includes an optimization with `myFlag` to stop early if no swaps are needed

```java
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
```

### ![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) Insertion Sort (`IterationSorting.java`)

- Builds the final sorted array one item at a time
- Efficient for small data sets or nearly sorted data
- Similar to how people sort playing cards in their hands

```java
for (int i = 1; i < numbers.length; i++) {
    int temp1 = numbers[i];
    int j = i - 1;
    while (j >= 0 && numbers[j] > temp1) {
        numbers[j+1] = numbers[j];
        numbers[j] = temp1;
        j--;
    }
}
```

### Merge Sort (`MergeSort.java`)

- Divide and conquer algorithm
- Recursively divides the array into halves until it can no more be divided
- Then merges the sorted halves

```java
public static void mergeSorting(int[] array) {
    if(array.length < 2) return;

    int midIndex = array.length / 2;
    int[] leftPart = new int[midIndex];
    int[] rightPart = new int[array.length - midIndex];

    // Split array into left and right
    mergeSorting(leftPart);
    mergeSorting(rightPart);
    merge(array, leftPart, rightPart);
}
```

### ![#00FF00](https://via.placeholder.com/15/00FF00/000000?text=+) Selection Sort (`SelectionSorting.java`)

- Divides the input list into a sorted and unsorted region
- Repeatedly selects the smallest (or largest) element from the unsorted region and moves it to the sorted region
- Simple but inefficient on large lists

```java
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
```

## Usage Examples

Each sorting class contains a `main` method that demonstrates the algorithm:

1. Creates an array of random integers
2. Prints the unsorted array
3. Sorts the array using the respective algorithm
4. Prints the sorted array

To run any sorting algorithm, simply execute its main class:

```bash
javac sorting/BubbleSort.java
java sorting.BubbleSort
```

## Performance Characteristics

| Algorithm          | Time Complexity | Space Complexity | Stable | Best For                             |
| ------------------ | --------------- | ---------------- | ------ | ------------------------------------ |
| **Bubble Sort**    | O(n²)           | O(1)             | Yes    | Educational purposes, small datasets |
| **Insertion Sort** | O(n²)           | O(1)             | Yes    | Small or nearly sorted datasets      |
| **Merge Sort**     | O(n log n)      | O(n)             | Yes    | Large datasets, requires stable sort |
| **Selection Sort** | O(n²)           | O(1)             | No     | When memory writes are expensive     |

## Visual Comparison

Here's a conceptual comparison of how the algorithms perform with different input sizes:

```
Elements    Bubble      Insertion    Merge       Selection
10          1ms         0.5ms        0.3ms       0.7ms
100         10ms        5ms          2ms         8ms
1000        100ms       50ms         20ms        80ms
10000       10000ms     5000ms       300ms       8000ms
```

_Note: Actual times will vary based on hardware and implementation details._

## How to Run

1. Ensure you have Java installed (JDK 8 or higher recommended)
2. Compile all Java files:
   ```bash
   javac sorting/*.java
   ```
3. Run any sorting algorithm:
   ```bash
   java sorting.BubbleSort
   java sorting.IterationSorting
   java sorting.MergeSort
   java sorting.SelectionSorting
   ```

### 📜 License

---

This files are open-source and available for educational use.
