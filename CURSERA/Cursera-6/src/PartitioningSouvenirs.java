import java.util.Scanner;

public class PartitioningSouvenirs {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfSouvenirs = scanner.nextInt();
        int[] values = new int[numberOfSouvenirs];
        int sum = 0;
        for (int i = 0; i < numberOfSouvenirs; i++) {
            values[i] = scanner.nextInt();
            sum += values[i];
        }
        System.out.println(canPartition(values, sum));
    }

    private static int canPartition(int[] values, int sum) {
        if (sum % 3 != 0) {
            return 0;
        }
        int target = sum / 3;
        int[][] possibleSums = new int[values.length + 1][target + 1];

        for (int i = 0; i <= values.length; i++) {
            possibleSums[i][0] = 1;
        }

        for (int i = 1; i <= values.length; i++) {
            for (int j = 1; j <= target; j++) {
                possibleSums[i][j] = possibleSums[i - 1][j];
                if (values[i - 1] <= j) {
                    if (possibleSums[i - 1][j - values[i - 1]] == 1) {
                        possibleSums[i][j] = 1;
                    }
                }
            }
        }

        if (possibleSums[values.length][target] == 1) {
            return canPartitionKSubsets(values, 3) ? 1 : 0;
        }

        return 0;
    }

    private static boolean canPartitionKSubsets(int[] souvenirValues, int numberOfSubsets) {
        int sum = 0;
        for (int num : souvenirValues) {
            sum += num;
        }
        if (numberOfSubsets <= 0 || sum % numberOfSubsets != 0) {
            return false;
        }
        boolean[] visited = new boolean[souvenirValues.length];
        return canPartitionRecursive(souvenirValues, visited, 0, numberOfSubsets, 0, sum / numberOfSubsets);
    }

    private static boolean canPartitionRecursive(int[] souvenirValues, boolean[] visited, int startIndex, int numberOfSubsets, int currentSum, int target) {
        if (numberOfSubsets == 1) {
            return true;
        }
        if (currentSum == target) {
            return canPartitionRecursive(souvenirValues, visited, 0, numberOfSubsets - 1, 0, target);
        }
        for (int i = startIndex; i < souvenirValues.length; i++) {
            if (!visited[i] && currentSum + souvenirValues[i] <= target) {
                visited[i] = true;
                if (canPartitionRecursive(souvenirValues, visited, i + 1, numberOfSubsets, currentSum + souvenirValues[i], target)) {
                    return true;
                }
                visited[i] = false;
            }
        }
        return false;
    }
}
