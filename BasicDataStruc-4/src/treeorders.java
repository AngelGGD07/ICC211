import java.util.*;
import java.io.*;

public class treeorders {
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

    public class TreeOrders {
        int n;
        int[] key, left, right;

        void read() throws IOException {
            FastScanner in = new FastScanner();
            n = in.nextInt();
            if (n == 0) return;
            key = new int[n];
            left = new int[n];
            right = new int[n];
            for (int i = 0; i < n; i++) {
                key[i] = in.nextInt();
                left[i] = in.nextInt();
                right[i] = in.nextInt();
            }
        }

        List<Integer> inOrder() {
            ArrayList<Integer> result = new ArrayList<Integer>();
            if (n == 0) return result;
            inOrderTraversal(0, result);
            return result;
        }

        private void inOrderTraversal(int index, List<Integer> result) {
            if (index == -1) {
                return;
            }
            inOrderTraversal(left[index], result);
            result.add(key[index]);
            inOrderTraversal(right[index], result);
        }

        List<Integer> preOrder() {
            ArrayList<Integer> result = new ArrayList<Integer>();
            if (n == 0) return result;
            preOrderTraversal(0, result);
            return result;
        }

        private void preOrderTraversal(int index, List<Integer> result) {
            if (index == -1) {
                return;
            }
            result.add(key[index]);
            preOrderTraversal(left[index], result);
            preOrderTraversal(right[index], result);
        }

        List<Integer> postOrder() {
            ArrayList<Integer> result = new ArrayList<Integer>();
            if (n == 0) return result;
            postOrderTraversal(0, result);
            return result;
        }

        private void postOrderTraversal(int index, List<Integer> result) {
            if (index == -1) {
                return;
            }
            postOrderTraversal(left[index], result);
            postOrderTraversal(right[index], result);
            result.add(key[index]);
        }
    }

    static public void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new treeorders().run();
                } catch (IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }

    public void print(List<Integer> x) {
        for (Integer a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        TreeOrders tree = new TreeOrders();
        tree.read();
        print(tree.inOrder());
        print(tree.preOrder());
        print(tree.postOrder());
    }
}
