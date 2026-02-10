import java.util.Scanner;

public class LastDigitOfTheSumOfFibonacciNumbersAgain {

    private static long getFibonacciPartialSumLastDigit(long from, long to) {
        // The last digit of the sum from F_from to F_to is
        // (LastDigit(Sum(to)) - LastDigit(Sum(from - 1)) + 10) % 10
        long lastDigitSumTo = getFibonacciSumLastDigit(to);
        long lastDigitSumFromMinus1 = (from == 0) ? 0 : getFibonacciSumLastDigit(from - 1);

        return (lastDigitSumTo - lastDigitSumFromMinus1 + 10) % 10;
    }

    // Calculates the last digit of the sum of the first n Fibonacci numbers
    private static long getFibonacciSumLastDigit(long n) {
        if (n <= 1) {
            return n;
        }
        // The sum of the first n Fibonacci numbers is F(n+2) - 1.
        // LastDigit(Sum) = (LastDigit(F(n+2)) - 1 + 10) % 10.
        int n_plus_2_mod_60 = (int)((n + 2) % 60);
        int lastDigitOfF_n_plus_2 = getFibonacciLastDigit(n_plus_2_mod_60);
        return (lastDigitOfF_n_plus_2 + 9) % 10;
    }

    // Helper function to get the last digit of F(m)
    private static int getFibonacciLastDigit(int m) {
        if (m <= 1) {
            return m;
        }
        int previous = 0;
        int current  = 1;
        for (int i = 0; i < m - 1; ++i) {
            int tmp_previous = previous;
            previous = current;
            current = (tmp_previous + current) % 10;
        }
        return current;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long from = scanner.nextLong();
        long to = scanner.nextLong();
        System.out.println(getFibonacciPartialSumLastDigit(from, to));
    }
}
