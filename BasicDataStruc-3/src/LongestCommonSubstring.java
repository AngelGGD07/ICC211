import java.io.*;
import java.util.HashSet;
import java.util.StringTokenizer;

public class LongestCommonSubstring {
    private static final long m1 = 1000000007;
    private static final long m2 = 1000000009;
    private static final long x = 263;

    public static void main(String[] args) throws IOException {
        new LongestCommonSubstring().run();
    }

    public void run() throws IOException {
        FastScanner in = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);
        String s, t;
        while ((s = in.next()) != null && (t = in.next()) != null) {
            Answer ans = solve(s, t);
            out.println(ans.i + " " + ans.j + " " + ans.len);
        }
        out.close();
    }

    protected Answer solve(String s, String t) {
        int low = 0, high = Math.min(s.length(), t.length());
        Answer bestAnswer = new Answer(0, 0, 0);

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (mid == 0) {
                low = mid + 1;
                continue;
            }
            Answer currentAnswer = check(s, t, mid);
            if (currentAnswer.len > 0) {
                bestAnswer = currentAnswer;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return bestAnswer;
    }

    private Answer check(String s, String t, int len) {
        long[] h1_s = precomputeHashes(s, m1);
        long[] h2_s = precomputeHashes(s, m2);
        long[] h1_t = precomputeHashes(t, m1);
        long[] h2_t = precomputeHashes(t, m2);

        long[] p1 = precomputePowers(len, m1);
        long[] p2 = precomputePowers(len, m2);

        HashSet<Long> hashesS = getHashes(s, len, h1_s, h2_s, p1, p2);

        for (int j = 0; j <= t.length() - len; j++) {
            long hash1 = (h1_t[j + len] - (p1[len] * h1_t[j]) % m1 + m1) % m1;
            long hash2 = (h2_t[j + len] - (p2[len] * h2_t[j]) % m2 + m2) % m2;
            long combinedHash = (hash1 << 32) | hash2;
            if (hashesS.contains(combinedHash)) {
                 for (int i = 0; i <= s.length() - len; i++) {
                     long s_hash1 = (h1_s[i + len] - (p1[len] * h1_s[i]) % m1 + m1) % m1;
                     long s_hash2 = (h2_s[i + len] - (p2[len] * h2_s[i]) % m2 + m2) % m2;
                     if(s_hash1 == hash1 && s_hash2 == hash2) {
                         return new Answer(i, j, len);
                     }
                }
            }
        }
        return new Answer(0, 0, 0);
    }

    private HashSet<Long> getHashes(String s, int len, long[] h1, long[] h2, long[] p1, long[] p2) {
        HashSet<Long> hashes = new HashSet<>();
        for (int i = 0; i <= s.length() - len; i++) {
            long hash1 = (h1[i + len] - (p1[len] * h1[i]) % m1 + m1) % m1;
            long hash2 = (h2[i + len] - (p2[len] * h2[i]) % m2 + m2) % m2;
            hashes.add((hash1 << 32) | hash2);
        }
        return hashes;
    }

    private long[] precomputeHashes(String str, long prime) {
        long[] h = new long[str.length() + 1];
        for (int i = 1; i <= str.length(); i++) {
            h[i] = (h[i - 1] * x + str.charAt(i - 1)) % prime;
        }
        return h;
    }

    private long[] precomputePowers(int len, long prime) {
        long[] p = new long[len + 1];
        p[0] = 1;
        for (int i = 1; i <= len; i++) {
            p[i] = (p[i - 1] * x) % prime;
        }
        return p;
    }

    protected static class Answer {
        protected int i, j, len;
        protected Answer(int i, int j, int len) {
            this.i = i;
            this.j = j;
            this.len = len;
        }
    }

    class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null) return null;
                st = new StringTokenizer(line);
            }
            return st.nextToken();
        }
    }
}