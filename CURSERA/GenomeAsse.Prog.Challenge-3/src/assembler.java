import java.io.*;
import java.util.*;

public class assembler {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> kmers = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            kmers.add(line);
        }

        if (kmers.isEmpty()) return;

        int k = kmers.get(0).length();
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Integer> outDegree = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        for (String kmer : kmers) {
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            nodes.add(prefix);
            nodes.add(suffix);
            adj.computeIfAbsent(prefix, x -> new ArrayList<>()).add(suffix);
            outDegree.put(prefix, outDegree.getOrDefault(prefix, 0) + 1);
            inDegree.put(suffix, inDegree.getOrDefault(suffix, 0) + 1);
        }

        String startNode = nodes.iterator().next();
        for (String node : nodes) {
            int out = outDegree.getOrDefault(node, 0);
            int in = inDegree.getOrDefault(node, 0);
            if (out > in) {
                startNode = node;
                break;
            }
        }

        List<String> path = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            String u = stack.peek();
            List<String> neighbors = adj.get(u);
            if (neighbors != null && !neighbors.isEmpty()) {
                String v = neighbors.remove(neighbors.size() - 1);
                stack.push(v);
            } else {
                path.add(stack.pop());
            }
        }

        Collections.reverse(path);
        StringBuilder sb = new StringBuilder();
        sb.append(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            sb.append(path.get(i).substring(k - 2));
        }
        System.out.println(sb.toString());
    }
}
