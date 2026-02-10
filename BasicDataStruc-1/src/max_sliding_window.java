import java.util.*;
import java.io.*;

public class max_sliding_window {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int m = scanner.nextInt();

        Deque<Integer> dq = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (!dq.isEmpty() && dq.peekFirst() == i - m) {
                dq.pollFirst();
            }
            while (!dq.isEmpty() && a[dq.peekLast()] < a[i]) {
                dq.pollLast();
            }
            dq.offerLast(i);
            if (i >= m - 1) {
                System.out.print(a[dq.peekFirst()] + " ");
            }
        }
    }
}
