import java.util.*;
import java.io.*;

public class universal_string {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.isEmpty()) return;
        
        int k = Integer.parseInt(line.trim());
        
        // Nodes: 0 to 2^(k-1) - 1
        int numNodes = 1 << (k - 1);
        int mask = numNodes - 1;
        
        // visited[u] stores which edges are used.
        // 0: none, 1: edge '0' used, 2: edge '1' used, 3: both used.
        int[] visited = new int[numNodes]; 
        
        Deque<Integer> stack = new ArrayDeque<>();
        List<Integer> path = new ArrayList<>();
        
        stack.push(0);
        
        while (!stack.isEmpty()) {
            int u = stack.peek();
            if (visited[u] < 3) {
                int bit;
                if ((visited[u] & 1) == 0) {
                    bit = 0;
                    visited[u] |= 1;
                } else {
                    bit = 1;
                    visited[u] |= 2;
                }
                
                int v = ((u << 1) & mask) | bit;
                stack.push(v);
            } else {
                path.add(stack.pop());
            }
        }
        
        Collections.reverse(path);
        
        StringBuilder sb = new StringBuilder();
        // The path contains nodes.
        // The edges are (path[i] -> path[i+1]).
        // The bit for edge i is path[i+1] & 1.
        
        // We want the string to start with k zeros.
        // The first node is 0...0 (k-1 zeros).
        // The first edge is 0 (bit 0).
        // So the sequence starts with k zeros if we prepend k-1 zeros.
        
        for (int i = 0; i < k - 1; i++) {
            sb.append('0');
        }
        for (int i = 1; i < path.size(); i++) {
            sb.append(path.get(i) & 1);
        }
        
        int length = 1 << k;
        if (sb.length() > length) {
            System.out.println(sb.substring(0, length));
        } else {
            System.out.println(sb.toString());
        }
    }
}
