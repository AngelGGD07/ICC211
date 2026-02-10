import java.util.Scanner;

public class LastDigitOfTheSumOfFibonacciNumbers {

    private static long getFibonacciSumLastDigit(long n) {
        if (n <= 1) {
            return n;
        }

        // The sum of the first n Fibonacci numbers is F(n+2) - 1.
        // We need the last digit of this sum.
        // LastDigit(Sum) = (LastDigit(F(n+2)) - 1 + 10) % 10.

        // The last digit of Fibonacci numbers repeats with a period of 60.
        int pisanoPeriod = 60;
        int n_plus_2_mod_60 = (int)((n + 2) % pisanoPeriod);

        // Calculate the last digit of F(n_plus_2_mod_60)
        int lastDigitOfF_n_plus_2 = getFibonacciLastDigit(n_plus_2_mod_60);

        // Now calculate (lastDigitOfF_n_plus_2 - 1 + 10) % 10
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
        long n = scanner.nextLong();
        long s = getFibonacciSumLastDigit(n);
        System.out.println(s);
    }
}
