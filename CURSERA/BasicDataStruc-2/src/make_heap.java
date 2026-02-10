import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class make_heap {
    private int[] data;
    private List<String> swaps;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new make_heap().solve();
    }

    private void readData() throws IOException {
        int n = in.nextInt();
        data = new int[n];
        for (int i = 0; i < n; ++i) {
            data[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        out.println(swaps.size());
        for (String swap : swaps) {
            out.println(swap);
        }
    }

    private void generateSwaps() {
        swaps = new ArrayList<String>();
        for (int i = data.length / 2; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void siftDown(int i) {
        int minIndex = i;
        int l = 2 * i + 1;
        if (l < data.length && data[l] < data[minIndex]) {
            minIndex = l;
        }
        int r = 2 * i + 2;
        if (r < data.length && data[r] < data[minIndex]) {
            minIndex = r;
        }
        if (i != minIndex) {
            swaps.add(i + " " + minIndex);
            int tmp = data[i];
            data[i] = data[minIndex];
            data[minIndex] = tmp;
            siftDown(minIndex);
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        generateSwaps();
        writeResponse();
        out.close();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
