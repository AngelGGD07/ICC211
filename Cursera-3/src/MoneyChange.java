import java.util.Scanner;

public class MoneyChange {

    private static int getChange(int m) {
        int n = 0;
        n += m / 10;
        m %= 10;
        n += m / 5;
        m %= 5;
        n += m;
        return n;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println(getChange(m));
    }
}
