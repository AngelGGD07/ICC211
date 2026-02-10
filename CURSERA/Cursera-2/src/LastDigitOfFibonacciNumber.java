import java.util.Scanner;

public class LastDigitOfFibonacciNumber {
    private static int getFibonacciLastDigit(int n) {
        if (n <= 1)
            return n;

        int[] fibLastDigits = new int[n + 1];
        fibLastDigits[0] = 0;
        fibLastDigits[1] = 1;

        for (int i = 2; i <= n; i++) {
            fibLastDigits[i] = (fibLastDigits[i - 1] + fibLastDigits[i - 2]) % 10;
        }

        return fibLastDigits[n];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(getFibonacciLastDigit(n));
    }
}
