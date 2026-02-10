import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FindSubstring {

    private static FastScanner in;
    private static PrintWriter out;
    private static final long PRIME = 1000000007;
    private static final long MULTIPLIER = 263;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        printOccurrences(getOccurrences(readInput()));
        out.close();
    }

    private static Data readInput() throws IOException {
        String pattern = in.next();
        String text = in.next();
        return new Data(pattern, text);
    }

    private static void printOccurrences(List<Integer> ans) {
        for (Integer cur : ans) {
            out.print(cur);
            out.print(" ");
        }
    }

    private static List<Integer> getOccurrences(Data input) {
        String pattern = input.pattern;
        String text = input.text;
        int patternLength = pattern.length();
        int textLength = text.length();
        List<Integer> occurrences = new ArrayList<>();

        if (patternLength > textLength) {
            return occurrences;
        }

        long patternHash = polyHash(pattern);
        long[] textHashes = precomputeHashes(text, patternLength);

        for (int i = 0; i <= textLength - patternLength; i++) {
            if (patternHash == textHashes[i]) {
                if (text.substring(i, i + patternLength).equals(pattern)) {
                    occurrences.add(i);
                }
            }
        }
        return occurrences;
    }

    private static long polyHash(String s) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            hash = (hash * MULTIPLIER + s.charAt(i)) % PRIME;
        }
        return hash;
    }

    private static long[] precomputeHashes(String text, int patternLength) {
        int textLength = text.length();
        long[] hashes = new long[textLength - patternLength + 1];
        String lastSubstring = text.substring(textLength - patternLength);
        hashes[textLength - patternLength] = polyHash(lastSubstring);

        long y = 1;
        for (int i = 0; i < patternLength; i++) {
            y = (y * MULTIPLIER) % PRIME;
        }

        for (int i = textLength - patternLength - 1; i >= 0; i--) {
            long hash = (MULTIPLIER * hashes[i + 1] + text.charAt(i) - y * text.charAt(i + patternLength)) % PRIME;
            hashes[i] = (hash + PRIME) % PRIME;
        }
        return hashes;
    }

    static class Data {
        String pattern;
        String text;
        public Data(String pattern, String text) {
            this.pattern = pattern;
            this.text = text;
        }
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
    }
}