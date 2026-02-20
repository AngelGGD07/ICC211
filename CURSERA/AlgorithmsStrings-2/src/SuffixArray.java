import java.util.*;
import java.io.*;

public class SuffixArray {
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

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        int n = text.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;

        Arrays.sort(order, (a, b) -> Character.compare(text.charAt(a), text.charAt(b)));

        int[] classArr = new int[n];
        classArr[order[0]] = 0;
        for (int i = 1; i < n; i++) {
            if (text.charAt(order[i]) != text.charAt(order[i - 1])) {
                classArr[order[i]] = classArr[order[i - 1]] + 1;
            } else {
                classArr[order[i]] = classArr[order[i - 1]];
            }
        }

        for (int len = 1; len < n; len *= 2) {
            int l = len;
            int[] currentClass = classArr;
            Arrays.sort(order, (a, b) -> {
                if (currentClass[a] != currentClass[b]) {
                    return Integer.compare(currentClass[a], currentClass[b]);
                }
                int nextA = (a + l < n) ? currentClass[a + l] : -1;
                int nextB = (b + l < n) ? currentClass[b + l] : -1;
                return Integer.compare(nextA, nextB);
            });

            int[] newClass = new int[n];
            newClass[order[0]] = 0;
            for (int i = 1; i < n; i++) {
                int prev = order[i - 1];
                int curr = order[i];
                int midPrev = (prev + l < n) ? currentClass[prev + l] : -1;
                int midCurr = (curr + l < n) ? currentClass[curr + l] : -1;

                if (currentClass[prev] != currentClass[curr] || midPrev != midCurr) {
                    newClass[curr] = newClass[prev] + 1;
                } else {
                    newClass[curr] = newClass[prev];
                }
            }
            classArr = newClass;
        }

        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = order[i];
        }
        return result;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArray().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] SuffixArray = computeSuffixArray(text);
        print(SuffixArray);
    }
}
