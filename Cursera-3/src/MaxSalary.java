import java.util.Arrays;
import java.util.Scanner;

public class MaxSalary {

    private static String largestNumber(String[] a) {
        Arrays.sort(a, (s1, s2) -> (s2 + s1).compareTo(s1 + s2));
        StringBuilder result = new StringBuilder();
        for (String s : a) {
            result.append(s);
        }
        String finalResult = result.toString();
        if (finalResult.matches("0+")) {
            return "0";
        }
        return finalResult;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.next();
        }
        System.out.println(largestNumber(a));
    }
}
