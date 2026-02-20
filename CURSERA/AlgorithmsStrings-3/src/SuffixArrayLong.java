import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixArrayLong {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        int[] order = sortCharacters(text);
        int[] classArray = computeCharClasses(text, order);
        int L = 1;
        while (L < text.length()) {
            order = sortDoubled(text, L, order, classArray);
            classArray = updateClasses(order, classArray, L);
            L = 2 * L;
        }
        return order;
    }

    private int[] sortCharacters(String S) {
        int[] order = new int[S.length()];
        int[] count = new int[5]; // $, A, C, G, T
        for (int i = 0; i < S.length(); i++) {
            count[charToIndex(S.charAt(i))]++;
        }
        for (int j = 1; j < 5; j++) {
            count[j] = count[j] + count[j - 1];
        }
        for (int i = S.length() - 1; i >= 0; i--) {
            int c = charToIndex(S.charAt(i));
            count[c]--;
            order[count[c]] = i;
        }
        return order;
    }

    private int charToIndex(char c) {
        switch (c) {
            case '$': return 0;
            case 'A': return 1;
            case 'C': return 2;
            case 'G': return 3;
            case 'T': return 4;
            default: return -1;
        }
    }

    private int[] computeCharClasses(String S, int[] order) {
        int[] classArray = new int[S.length()];
        classArray[order[0]] = 0;
        for (int i = 1; i < S.length(); i++) {
            if (S.charAt(order[i]) != S.charAt(order[i - 1])) {
                classArray[order[i]] = classArray[order[i - 1]] + 1;
            } else {
                classArray[order[i]] = classArray[order[i - 1]];
            }
        }
        return classArray;
    }

    private int[] sortDoubled(String S, int L, int[] order, int[] classArray) {
        int[] count = new int[S.length()];
        int[] newOrder = new int[S.length()];
        for (int i = 0; i < S.length(); i++) {
            count[classArray[i]]++;
        }
        for (int j = 1; j < S.length(); j++) {
            count[j] = count[j] + count[j - 1];
        }
        for (int i = S.length() - 1; i >= 0; i--) {
            int start = (order[i] - L + S.length()) % S.length();
            int cl = classArray[start];
            count[cl]--;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    private int[] updateClasses(int[] newOrder, int[] classArray, int L) {
        int n = newOrder.length;
        int[] newClassArray = new int[n];
        newClassArray[newOrder[0]] = 0;
        for (int i = 1; i < n; i++) {
            int cur = newOrder[i];
            int prev = newOrder[i - 1];
            int mid = (cur + L) % n;
            int midPrev = (prev + L) % n;
            if (classArray[cur] != classArray[prev] || classArray[mid] != classArray[midPrev]) {
                newClassArray[cur] = newClassArray[prev] + 1;
            } else {
                newClassArray[cur] = newClassArray[prev];
            }
        }
        return newClassArray;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
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
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
