import java.util.*;
import java.io.*;

public class is_bst {
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

    public class IsBST {
        class Node {
            int key;
            int left;
            int right;

            Node(int key, int left, int right) {
                this.left = left;
                this.right = right;
                this.key = key;
            }
        }

        int nodes;
        Node[] tree;

        void read() throws IOException {
            FastScanner in = new FastScanner();
            nodes = in.nextInt();
            if (nodes == 0) {
                return;
            }
            tree = new Node[nodes];
            for (int i = 0; i < nodes; i++) {
                tree[i] = new Node(in.nextInt(), in.nextInt(), in.nextInt());
            }
        }

        boolean solve() {
            if (nodes == 0) {
                return true;
            }
            return isBSTUtil(0, Long.MIN_VALUE, Long.MAX_VALUE);
        }

        boolean isBSTUtil(int index, long min, long max) {
            if (index == -1) {
                return true;
            }

            if (tree[index].key <= min || tree[index].key >= max) {
                return false;
            }

            return isBSTUtil(tree[index].left, min, tree[index].key) &&
                   isBSTUtil(tree[index].right, tree[index].key, max);
        }
    }

    static public void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new is_bst().run();
                } catch (IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }
    public void run() throws IOException {
        IsBST tree = new IsBST();
        tree.read();
        // The original skeleton had a call to a non-existent `solve` method.
        // I have renamed `isBinarySearchTree` to `solve` to fix this.
        if (tree.solve()) {
            System.out.println("CORRECT");
        } else {
            System.out.println("INCORRECT");
        }
    }
}
