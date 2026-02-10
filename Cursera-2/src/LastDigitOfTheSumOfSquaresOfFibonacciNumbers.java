import java.util.Scanner;

public class LastDigitOfTheSumOfSquaresOfFibonacciNumbers {
    private static long getFibonacciSumSquares(long n) {
        if (n <= 1) {
            return n;
        }

        // The sum of squares of the first n Fibonacci numbers is F(n) * F(n+1).
        // We need the last digit of this product.
        // The last digit of Fibonacci numbers repeats with a period of 60.
        int pisanoPeriod = 60;
        int n_mod_60 = (int)(n % pisanoPeriod);
        int n_plus_1_mod_60 = (int)((n + 1) % pisanoPeriod);

        int fn_last_digit = getFibonacciLastDigit(n_mod_60);
        int fn_plus_1_last_digit = getFibonacciLastDigit(n_plus_1_mod_60);

        return (long)(fn_last_digit * fn_plus_1_last_digit) % 10;
    }

    // A robust implementation to get the last digit of the nth Fibonacci number.
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
        long n = scanner.nextLong();
        long s = getFibonacciSumSquares(n);
        System.out.println(s);
    }
}
