import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        int n = scanner.nextInt();
        long[] numbers = new long[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextLong();
        }

        System.out.println(maxPairwiseProduct(numbers));
    }

    static long maxPairwiseProduct(long[] numbers) {
        int n = numbers.length;

        if (n < 2) {
            return 0; // No pairs exist, so the product is 0.
        }

        long max1 = Long.MIN_VALUE;
        long max2 = Long.MIN_VALUE;
        long min1 = Long.MAX_VALUE;
        long min2 = Long.MAX_VALUE;

        for (long number : numbers) {
            if (number > max1) {
                max2 = max1;
                max1 = number;
            } else if (number > max2) {
                max2 = number;
            }

            if (number < min1) {
                min2 = min1;
                min1 = number;
            } else if (number < min2) {
                min2 = number;
            }
        }

        return Math.max(max1 * max2, min1 * min2);
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }
}
