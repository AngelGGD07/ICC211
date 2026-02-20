import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SuffixTreeFromArray {
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

    // Data structure to store edges of a suffix tree.
    public class Edge {
        // The ending node of this edge.
        int node;
        // Starting position of the substring of the text
        // corresponding to the label of this edge.
        int start;
        // Position right after the end of the substring of the text
        // corresponding to the label of this edge.
        int end;

        Edge(int node, int start, int end) {
            this.node = node;
            this.start = start;
            this.end = end;
        }
    }

    class NodeDepth {
        int node;
        int depth;

        NodeDepth(int node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    // Build suffix tree of the string text given its suffix array suffix_array
    // and LCP array lcp_array. Return the tree as a mapping from a node ID
    // to the list of all outgoing edges of the corresponding node. The edges in the
    // list must be sorted in the ascending order by the first character of the edge label.
    // Root must have node ID = 0, and all other node IDs must be different
    // nonnegative integers.
    //
    // For example, if text = "ACACAA$", an edge with label "$" from root to a node with ID 1
    // must be represented by new Edge(1, 6, 7). This edge must be present in the list tree.get(0)
    // (corresponding to the root node), and it should be the first edge in the list
    // (because it has the smallest first character of all edges outgoing from the root).
    Map<Integer, List<Edge>> SuffixTreeFromSuffixArray(
            int[] suffixArray,
            int[] lcpArray,
            final String text) {
        Map<Integer, List<Edge>> tree = new HashMap<>();
        int root = 0;
        int nodesCount = 1;
        tree.put(root, new ArrayList<>());

        Stack<NodeDepth> stack = new Stack<>();
        stack.push(new NodeDepth(root, 0));

        for (int i = 0; i < suffixArray.length; i++) {
            int suffix = suffixArray[i];
            int lcp = 0;
            if (i > 0) {
                lcp = lcpArray[i - 1];
            }

            while (stack.peek().depth > lcp) {
                stack.pop();
            }

            NodeDepth parent = stack.peek();
            if (parent.depth == lcp) {
                int leaf = nodesCount++;
                tree.put(leaf, new ArrayList<>());
                tree.get(parent.node).add(new Edge(leaf, suffix + lcp, text.length()));
                stack.push(new NodeDepth(leaf, text.length() - suffix));
            } else {
                // We need to split the edge leading to lastPopped.
                // The edge starts at parent.depth (offset in text).
                // The edge connects parent.node to lastPopped.node.
                // We need to find this edge in parent's edge list.
                // Since it's the rightmost path, it should be the last edge added to parent?
                // Yes, because we process in suffix array order.

                List<Edge> edges = tree.get(parent.node);
                Edge edgeToSplit = edges.get(edges.size() - 1);

                int edgeSpan = lcp - parent.depth;
                int internalNode = nodesCount++;
                tree.put(internalNode, new ArrayList<>());

                // Update edgeToSplit to point to internalNode and end at split point.
                int originalStart = edgeToSplit.start;
                int originalEnd = edgeToSplit.end;
                int originalDest = edgeToSplit.node;

                edgeToSplit.end = originalStart + edgeSpan;
                edgeToSplit.node = internalNode;

                // Add edge from internalNode to originalDest
                tree.get(internalNode).add(new Edge(originalDest, originalStart + edgeSpan, originalEnd));

                // Add edge from internalNode to new leaf
                int leaf = nodesCount++;
                tree.put(leaf, new ArrayList<>());
                tree.get(internalNode).add(new Edge(leaf, suffix + lcp, text.length()));

                stack.push(new NodeDepth(internalNode, lcp));
                stack.push(new NodeDepth(leaf, text.length() - suffix));
            }
        }

        return tree;
    }


    static public void main(String[] args) throws IOException {
        new SuffixTreeFromArray().run();
    }

    public void print(ArrayList<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffixArray = new int[text.length()];
        for (int i = 0; i < suffixArray.length; ++i) {
            suffixArray[i] = scanner.nextInt();
        }
        int[] lcpArray = new int[text.length() - 1];
        for (int i = 0; i + 1 < text.length(); ++i) {
            lcpArray[i] = scanner.nextInt();
        }
        System.out.println(text);
        // Build the suffix tree and get a mapping from
        // suffix tree node ID to the list of outgoing Edges.
        Map<Integer, List<Edge>> suffixTree = SuffixTreeFromSuffixArray(suffixArray, lcpArray, text);
        ArrayList<String> result = new ArrayList<>();
        // Output the edges of the suffix tree in the required order.
        // Note that we use here the contract that the root of the tree
        // will have node ID = 0 and that each vector of outgoing edges
        // will be sorted by the first character of the corresponding edge label.
        //
        // The following code avoids recursion to avoid stack overflow issues.
        // It uses two stacks to convert recursive function to a while loop.
        // This code is an equivalent of
        //
        //    OutputEdges(tree, 0);
        //
        // for the following _recursive_ function OutputEdges:
        //
        // public void OutputEdges(Map<Integer, List<Edge>> tree, int nodeId) {
        //     List<Edge> edges = tree.get(nodeId);
        //     for (Edge edge : edges) {
        //         System.out.println(edge.start + " " + edge.end);
        //         OutputEdges(tree, edge.node);
        //     }
        // }
        //
        int[] nodeStack = new int[2 * text.length() + 10]; // Increased size just in case
        int[] edgeIndexStack = new int[2 * text.length() + 10];
        nodeStack[0] = 0;
        edgeIndexStack[0] = 0;
        int stackSize = 1;
        while (stackSize > 0) {
            int node = nodeStack[stackSize - 1];
            int edgeIndex = edgeIndexStack[stackSize - 1];
            
            if (suffixTree.get(node) == null) {
                stackSize -= 1;
                continue;
            }
            if (edgeIndex < suffixTree.get(node).size()) {
                edgeIndexStack[stackSize - 1] = edgeIndex + 1;
                
                Edge edge = suffixTree.get(node).get(edgeIndex);
                result.add(edge.start + " " + edge.end);
                
                nodeStack[stackSize] = edge.node;
                edgeIndexStack[stackSize] = 0;
                stackSize += 1;
            } else {
                stackSize -= 1;
            }
        }
        print(result);
    }
}
