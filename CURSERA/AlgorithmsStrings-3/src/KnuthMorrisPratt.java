import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KnuthMorrisPratt {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    // Find all the occurrences of the pattern in the text and return
    // a list of all positions in the text (starting from 0) where
    // the pattern starts in the text.
    public List<Integer> findPattern(String pattern, String text) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int[] s = computePrefixFunction(pattern);
        int border = 0;
        for (int i = 0; i < text.length(); i++) {
            while (border > 0 && text.charAt(i) != pattern.charAt(border)) {
                border = s[border - 1];
            }
            if (text.charAt(i) == pattern.charAt(border)) {
                border++;
            }
            if (border == pattern.length()) {
                result.add(i - pattern.length() + 1);
                border = s[border - 1];
            }
        }
        return result;
    }

    private int[] computePrefixFunction(String P) {
        int[] s = new int[P.length()];
        s[0] = 0;
        int border = 0;
        for (int i = 1; i < P.length(); i++) {
            while (border > 0 && P.charAt(i) != P.charAt(border)) {
                border = s[border - 1];
            }
            if (P.charAt(i) == P.charAt(border)) {
                border++;
            }
            s[i] = border;
        }
        return s;
    }

    static public void main(String[] args) throws IOException {
        new KnuthMorrisPratt().run();
    }

    public void print(List<Integer> x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String pattern = scanner.next();
        String text = scanner.next();
        List<Integer> positions = findPattern(pattern, text);
        print(positions);
    }
}
