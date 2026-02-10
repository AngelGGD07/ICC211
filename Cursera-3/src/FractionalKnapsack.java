import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class FractionalKnapsack {

    private static double getOptimalValue(int capacity, int[] values, int[] weights) {
        double value = 0.0;
        int numItems = values.length;
        ItemValue[] items = new ItemValue[numItems];
        for (int i = 0; i < numItems; i++) {
            items[i] = new ItemValue(values[i], weights[i]);
        }

        Arrays.sort(items, new Comparator<ItemValue>() {
            @Override
            public int compare(ItemValue o1, ItemValue o2) {
                return o2.valuePerUnit.compareTo(o1.valuePerUnit);
            }
        });

        for (ItemValue item : items) {
            if (capacity == 0) {
                return value;
            }
            int amount = Math.min(item.weight, capacity);
            value += amount * item.valuePerUnit;
            capacity -= amount;
        }

        return value;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int capacity = Integer.parseInt(st.nextToken());
        int[] values = new int[n];
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            values[i] = Integer.parseInt(st.nextToken());
            weights[i] = Integer.parseInt(st.nextToken());
        }
        System.out.printf("%.4f\n", getOptimalValue(capacity, values, weights));
    }

    private static class ItemValue {
        Double valuePerUnit;
        int weight;
        int value;

        public ItemValue(int value, int weight) {
            this.value = value;
            this.weight = weight;
            this.valuePerUnit = (double) value / weight;
        }
    }
}
