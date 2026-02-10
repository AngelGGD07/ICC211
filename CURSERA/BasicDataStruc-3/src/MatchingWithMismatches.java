import java.io.*;
import java.util.*;

public class MatchingWithMismatches {

    public static void main(String[] args) {
        new MatchingWithMismatches().run();
    }

    public void run() {
        FastScanner in = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);
        while (true) {
            try {
                int k = in.nextInt();
                String text = in.next();
                String pattern = in.next();
                List<Integer> ans = solve(k, text, pattern);
                out.print(ans.size());
                for (int x : ans) {
                    out.print(" " + x);
                }
                out.println();
            } catch (Exception e) {
                break;
            }
        }
        out.close();
    }

    private List<Integer> solve(int k, String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int textLen = text.length();
        int patternLen = pattern.length();
        if (patternLen > textLen) {
            return matches;
        }

        long p1 = 1000000007;
        long p2 = 1000000009;
        long x = new Random().nextInt((int) p1 - 2) + 1;

        long[] h1_text = precomputeHashes(text, p1, x);
        long[] h2_text = precomputeHashes(text, p2, x);
        long[] h1_pattern = precomputeHashes(pattern, p1, x);
        long[] h2_pattern = precomputeHashes(pattern, p2, x);

        long[] pow1 = precomputePowers(patternLen, p1, x);
        long[] pow2 = precomputePowers(patternLen, p2, x);

        for (int i = 0; i <= textLen - patternLen; i++) {
            if (countMismatches(h1_text, h2_text, h1_pattern, h2_pattern, i, 0, patternLen, pow1, pow2, p1, p2, k) <= k) {
                matches.add(i);
            }
        }
        return matches;
    }

    private int countMismatches(long[] h1_text, long[] h2_text, long[] h1_pattern, long[] h2_pattern, int text_start, int pattern_start, int len, long[] pow1, long[] pow2, long p1, long p2, int k) {
        if (len == 0) {
            return 0;
        }
        if (k < 0) {
            return 1_000_000;
        }

        int l = 0, r = len;
        int matching_prefix_len = 0;
        while (l <= r) {
            int mid_len = l + (r - l) / 2;
            if (areSubstringsEqual(h1_text, h1_pattern, text_start, pattern_start, mid_len, pow1, p1) &&
                areSubstringsEqual(h2_text, h2_pattern, text_start, pattern_start, mid_len, pow2, p2)) {
                matching_prefix_len = mid_len;
                l = mid_len + 1;
            } else {
                r = mid_len - 1;
            }
        }

        if (matching_prefix_len == len) {
            return 0;
        }

        int next_text_start = text_start + matching_prefix_len + 1;
        int next_pattern_start = pattern_start + matching_prefix_len + 1;
        int next_len = len - (matching_prefix_len + 1);

        return 1 + countMismatches(h1_text, h2_text, h1_pattern, h2_pattern, next_text_start, next_pattern_start, next_len, pow1, pow2, p1, p2, k - 1);
    }

    private long[] precomputeHashes(String s, long p, long x) {
        long[] h = new long[s.length() + 1];
        for (int i = 1; i <= s.length(); i++) {
            h[i] = (h[i - 1] * x + s.charAt(i - 1)) % p;
            if (h[i] < 0) h[i] += p;
        }
        return h;
    }

    private long[] precomputePowers(int len, long p, long x) {
        long[] pow = new long[len + 1];
        pow[0] = 1;
        for (int i = 1; i <= len; i++) {
            pow[i] = (pow[i - 1] * x) % p;
        }
        return pow;
    }

    private boolean areSubstringsEqual(long[] h_text, long[] h_pattern, int start_text, int start_pattern, int len, long[] pow, long p) {
        if (len == 0) return true;
        long hash_text = (h_text[start_text + len] - (pow[len] * h_text[start_text]) % p + p) % p;
        long hash_pattern = (h_pattern[start_pattern + len] - (pow[len] * h_pattern[start_pattern]) % p + p) % p;
        return hash_text == hash_pattern;
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}