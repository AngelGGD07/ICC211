import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaxPrizes {

    private static List<Integer> optimalSummands(int n) {
        List<Integer> summands = new ArrayList<>();
        for (int k = n, l = 1; k > 0; l++) {
            if (k <= 2 * l) {
                summands.add(k);
                k = 0;
            } else {
                summands.add(l);
                k -= l;
            }
        }
        return summands;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> summands = optimalSummands(n);
        System.out.println(summands.size());
        for (Integer summand : summands) {
            System.out.print(summand + " ");
        }
    }
}
