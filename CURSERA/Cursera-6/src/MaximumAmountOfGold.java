import java.util.Scanner;

public class MaximumAmountOfGold {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int capacity = scanner.nextInt();
        int numberOfItems = scanner.nextInt();
        int[] weights = new int[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            weights[i] = scanner.nextInt();
        }
        System.out.println(maxGold(capacity, weights));
    }

    private static int maxGold(int capacity, int[] weights) {
        int[][] maxValue = new int[weights.length + 1][capacity + 1];

        for (int itemIndex = 1; itemIndex <= weights.length; itemIndex++) {
            for (int currentCapacity = 1; currentCapacity <= capacity; currentCapacity++) {
                maxValue[itemIndex][currentCapacity] = maxValue[itemIndex - 1][currentCapacity];
                if (weights[itemIndex - 1] <= currentCapacity) {
                    int currentValue = maxValue[itemIndex - 1][currentCapacity - weights[itemIndex - 1]] + weights[itemIndex - 1];
                    if (currentValue > maxValue[itemIndex][currentCapacity]) {
                        maxValue[itemIndex][currentCapacity] = currentValue;
                    }
                }
            }
        }
        return maxValue[weights.length][capacity];
    }
}
