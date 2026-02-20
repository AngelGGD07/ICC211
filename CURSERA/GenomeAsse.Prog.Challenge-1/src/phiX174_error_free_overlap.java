import java.util.*;
import java.io.*;

public class phiX174_error_free_overlap {
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
        int[][] overlap = new int[n][n];
        
        // Calculate overlaps
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                overlap[i][j] = calculateOverlap(reads.get(i), reads.get(j));
            }
        }
        
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
                // Fallback if no overlap found
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
        
        // Construct the genome
        StringBuilder genome = new StringBuilder();
        genome.append(reads.get(path.get(0)));
        
        for (int i = 0; i < path.size() - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);
            int ov = overlap[u][v];
            genome.append(reads.get(v).substring(ov));
        }
        
        // Handle circularity
        int lastOv = overlap[path.get(path.size() - 1)][path.get(0)];
        if (genome.length() > lastOv) {
            genome.setLength(genome.length() - lastOv);
        }
        
        return genome.toString();
    }
    
    private static int calculateOverlap(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int minLen = Math.min(len1, len2);
        
        for (int k = minLen; k >= 1; k--) {
            if (s1.regionMatches(len1 - k, s2, 0, k)) {
                return k;
            }
        }
        return 0;
    }
}