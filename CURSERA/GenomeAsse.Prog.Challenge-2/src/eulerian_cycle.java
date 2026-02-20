import java.util.*;
import java.io.*;

public class eulerian_cycle {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null) return;
        
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2) return;
        
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        
        Map<Integer, List<Integer>> adj = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();
        Map<Integer, Integer> outDegree = new HashMap<>();
        
        for (int i = 0; i < m; i++) {
            line = br.readLine();
            if (line == null) break;
            String[] edge = line.trim().split("\\s+");
            int u = Integer.parseInt(edge[0]);
            int v = Integer.parseInt(edge[1]);
            
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            outDegree.put(u, outDegree.getOrDefault(u, 0) + 1);
            inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
            
            // Ensure v is in maps
            outDegree.putIfAbsent(v, 0);
            inDegree.putIfAbsent(u, 0);
        }
        
        // Check balance
        for (Integer node : outDegree.keySet()) {
            if (!outDegree.get(node).equals(inDegree.getOrDefault(node, 0))) {
                System.out.println("0");
                return;
            }
        }
        for (Integer node : inDegree.keySet()) {
            if (!inDegree.get(node).equals(outDegree.getOrDefault(node, 0))) {
                System.out.println("0");
                return;
            }
        }
        
        if (adj.isEmpty()) {
            System.out.println("1");
            System.out.println();
            return;
        }

        // Find cycle
        // Start at any node with edges
        int startNode = adj.keySet().iterator().next();
        
        Deque<Integer> stack = new ArrayDeque<>();
        List<Integer> circuit = new ArrayList<>();
        
        stack.push(startNode);
        
        while (!stack.isEmpty()) {
            int u = stack.peek();
            if (adj.containsKey(u) && !adj.get(u).isEmpty()) {
                List<Integer> neighbors = adj.get(u);
                int v = neighbors.remove(neighbors.size() - 1);
                stack.push(v);
            } else {
                circuit.add(stack.pop());
            }
        }
        
        if (circuit.size() != m + 1) {
            // Graph not connected (edges in different components)
            System.out.println("0");
            return;
        }
        
        Collections.reverse(circuit);
        
        System.out.println("1");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < circuit.size() - 1; i++) { // Output edges? Or nodes?
            // Example output: 1 2 2 3
            // Path: 1->2->2->3->1
            // If we output nodes, it should be 1 2 2 3 1?
            // Or maybe the last one is omitted?
            // The example output 1 2 2 3 has length 4. Edges = 4.
            // So it outputs the start node of each edge?
            // 1 (start of 1->2), 2 (start of 2->2), 2 (start of 2->3), 3 (start of 3->1).
            // Yes, that matches.
            sb.append(circuit.get(i)).append(" ");
        }
        // Append the last one? No, if it's start of edges.
        // Wait, if I output 1 2 2 3, the last node 1 is not printed.
        // Let's try printing all nodes except the very last one (which duplicates the first).
        // circuit has m+1 nodes.
        // We print m nodes?
        // Let's print circuit excluding the last element.
        
        System.out.println(sb.toString().trim());
    }
}
