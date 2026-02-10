import java.util.Scanner;

public class MoneyChangeAgain {
    private static int getChange(int money) {
        int[] minCoins = new int[money + 1];
        int[] coins = {1, 3, 4};

        for (int amount = 1; amount <= money; amount++) {
            minCoins[amount] = Integer.MAX_VALUE;
            for (int coin : coins) {
                if (amount >= coin) {
                    int numCoins = minCoins[amount - coin] + 1;
                    if (numCoins < minCoins[amount]) {
                        minCoins[amount] = numCoins;
                    }
                }
            }
        }
        return minCoins[money];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int money = scanner.nextInt();
        System.out.println(getChange(money));
    }
}
