import java.util.*;
import java.io.*;
import java.util.stream.IntStream;

public class phiX174_error_prone_overlap {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> reads = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            reads.add(line);
        }
        
        if (reads.isEmpty()) {
            return;
        }
        
        System.out.println(solve(reads));
    }

    private static String solve(List<String> reads) {
        int n = reads.size();
        char[][] readChars = new char[n][];
        for(int i=0; i<n; i++) readChars[i] = reads.get(i).toCharArray();
        
        int[][] overlap = new int[n][n];
        
        // Parallelize overlap calculation
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                overlap[i][j] = calculateOverlap(readChars[i], readChars[j]);
            }
        });
        
        boolean[] visited = new boolean[n];
        int current = 0;
        visited[0] = true;
        List<Integer> path = new ArrayList<>();
        path.add(0);
        
        for (int step = 0; step < n - 1; step++) {
            int bestNext = -1;
            int maxOv = -1;
            
            for (int j = 0; j < n; j++) {
                if (!visited[j]) {
                    if (overlap[current][j] > maxOv) {
                        maxOv = overlap[current][j];
                        bestNext = j;
                    }
                }
            }
            
            if (bestNext != -1) {
                visited[bestNext] = true;
                current = bestNext;
                path.add(current);
            } else {
                 // Fallback
                 for (int j = 0; j < n; j++) {
                    if (!visited[j]) {
                        bestNext = j;
                        break;
                    }
                }
                if (bestNext != -1) {
                     visited[bestNext] = true;
                     current = bestNext;
                     path.add(current);
                } else {
                    break;
                }
            }
        }
        
        // Consensus Assembly
        return buildConsensus(path, overlap, readChars);
    }
    
    private static int calculateOverlap(char[] s1, char[] s2) {
        int len1 = s1.length;
        int len2 = s2.length;
        // Optimization: Only check overlaps >= 30
        int minOverlap = 30; 
        int minLen = Math.min(len1, len2);
        
        for (int k = minLen; k >= minOverlap; k--) {
            if (isMatch(s1, len1 - k, s2, 0, k)) {
                return k;
            }
        }
        return 0;
    }
    
    private static boolean isMatch(char[] s1, int start1, char[] s2, int start2, int length) {
        int mismatches = 0;
        // Allow approx 2% error.
        int allowedMismatches = 2; 
        
        for (int i = 0; i < length; i++) {
            if (s1[start1 + i] != s2[start2 + i]) {
                mismatches++;
                if (mismatches > allowedMismatches) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String buildConsensus(List<Integer> path, int[][] overlap, char[][] readChars) {
        int n = path.size();
        long[] offsets = new long[n];
        offsets[0] = 0;
        for (int i = 0; i < n - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);
            int ov = overlap[u][v];
            offsets[i+1] = offsets[i] + (readChars[u].length - ov);
        }
        
        int last = path.get(n - 1);
        int first = path.get(0);
        int lastOv = overlap[last][first];
        
        long totalLengthLong = offsets[n - 1] + (readChars[last].length - lastOv);
        int totalLength = (int) totalLengthLong;
        
        if (totalLength <= 0) return "";
        
        int[][] counts = new int[totalLength][4]; // A, C, G, T
        
        for (int i = 0; i < n; i++) {
            int u = path.get(i);
            long start = offsets[i];
            char[] read = readChars[u];
            for (int j = 0; j < read.length; j++) {
                int pos = (int)((start + j) % totalLength);
                if (pos < 0) pos += totalLength;
                
                switch (read[j]) {
                    case 'A': counts[pos][0]++; break;
                    case 'C': counts[pos][1]++; break;
                    case 'G': counts[pos][2]++; break;
                    case 'T': counts[pos][3]++; break;
                }
            }
        }
        
        StringBuilder sb = new StringBuilder(totalLength);
        char[] bases = {'A', 'C', 'G', 'T'};
        for (int i = 0; i < totalLength; i++) {
            int maxCount = -1;
            int maxIdx = 0;
            for (int b = 0; b < 4; b++) {
                if (counts[i][b] > maxCount) {
                    maxCount = counts[i][b];
                    maxIdx = b;
                }
            }
            sb.append(bases[maxIdx]);
        }
        return sb.toString();
    }
}