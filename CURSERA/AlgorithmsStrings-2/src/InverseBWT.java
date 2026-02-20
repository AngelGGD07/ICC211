import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {
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

    String inverseBWT(String bwt) {
        int len = bwt.length();
        if (len == 0) return "";

        Map<Character, Integer> counts = new HashMap<>();
        for (int i = 0; i < len; i++) {
            char c = bwt.charAt(i);
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        List<Character> sortedChars = new ArrayList<>(counts.keySet());
        Collections.sort(sortedChars);

        Map<Character, Integer> starts = new HashMap<>();
        int currentPos = 0;
        for (char c : sortedChars) {
            starts.put(c, currentPos);
            currentPos += counts.get(c);
        }

        int[] next = new int[len];
        Map<Character, Integer> currentCounts = new HashMap<>();
        for (int i = 0; i < len; i++) {
            char c = bwt.charAt(i);
            int count = currentCounts.getOrDefault(c, 0);
            next[i] = starts.get(c) + count;
            currentCounts.put(c, count + 1);
        }

        char[] result = new char[len];
        // The problem statement usually implies '$' is the end of string marker.
        // However, if '$' is not present or we just need to reconstruct the cyclic shift
        // that corresponds to the original string (which ends with '$'), we need to find the row
        // that corresponds to the original string.
        // In BWT, the row corresponding to the original string ends with the last character of the original string.
        // But wait, the BWT is the last column.
        // The original string S ends with '$'.
        // So in the matrix of all cyclic shifts, the row that represents S starts with S[0] and ends with '$'.
        // This row is somewhere in the sorted matrix.
        // But we are given BWT(S).
        // We need to reconstruct S.
        // We know S ends with '$'.
        // The character before '$' is bwt[row starting with '$'].
        // Let's assume '$' is unique and is the lexicographically smallest character.
        // Then the row starting with '$' is row 0.
        
        int cur = 0; // Row starting with '$' (assuming '$' is smallest)
        // If '$' is not the smallest, we should find starts.get('$').
        if (starts.containsKey('$')) {
            cur = starts.get('$');
        } else {
             // Fallback or error handling if '$' is expected but not found.
             // For now, let's assume standard BWT problem where '$' is present.
             return "";
        }

        result[len - 1] = '$';

        for (int i = len - 2; i >= 0; i--) {
            result[i] = bwt.charAt(cur);
            cur = next[cur];
        }

        return new String(result);
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}
