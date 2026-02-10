import java.io.*;
import java.util.*;

class RopeProblem {
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

    static class Vertex {
        char key;
        int size;
        Vertex left;
        Vertex right;
        Vertex parent;

        Vertex(char key, int size, Vertex left, Vertex right, Vertex parent) {
            this.key = key;
            this.size = size;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    static class VertexPair {
        Vertex left;
        Vertex right;
        VertexPair(Vertex left, Vertex right) {
            this.left = left;
            this.right = right;
        }
    }

    static void update(Vertex v) {
        if (v == null) return;
        v.size = 1 + (v.left != null ? v.left.size : 0) + (v.right != null ? v.right.size : 0);
        if (v.left != null) {
            v.left.parent = v;
        }
        if (v.right != null) {
            v.right.parent = v;
        }
    }

    static void smallRotation(Vertex v) {
        Vertex parent = v.parent;
        if (parent == null) {
            return;
        }
        Vertex grandparent = v.parent.parent;
        if (parent.left == v) {
            Vertex m = v.right;
            v.right = parent;
            parent.left = m;
        } else {
            Vertex m = v.left;
            v.left = parent;
            parent.right = m;
        }
        update(parent);
        update(v);
        v.parent = grandparent;
        if (grandparent != null) {
            if (grandparent.left == parent) {
                grandparent.left = v;
            } else {
                grandparent.right = v;
            }
        }
    }

    static void bigRotation(Vertex v) {
        if (v.parent.left == v && v.parent.parent.left == v.parent) {
            smallRotation(v.parent);
            smallRotation(v);
        } else if (v.parent.right == v && v.parent.parent.right == v.parent) {
            smallRotation(v.parent);
            smallRotation(v);
        } else {
            smallRotation(v);
            smallRotation(v);
        }
    }

    static Vertex splay(Vertex v) {
        if (v == null) return null;
        while (v.parent != null) {
            if (v.parent.parent == null) {
                smallRotation(v);
                break;
            }
            bigRotation(v);
        }
        return v;
    }

    static Vertex find(Vertex root, int index) {
        Vertex v = root;
        Vertex last = root;
        while (v != null) {
            last = v;
            int leftSize = (v.left != null) ? v.left.size : 0;
            if (index == leftSize) {
                break;
            }
            if (index < leftSize) {
                v = v.left;
            } else {
                index -= leftSize + 1;
                v = v.right;
            }
        }
        return splay(last);
    }

    static VertexPair split(Vertex root, int index) {
        if (root == null) return new VertexPair(null, null);
        if (index >= root.size) {
            Vertex newRoot = find(root, root.size - 1);
            return new VertexPair(newRoot, null);
        }
        Vertex newRoot = find(root, index);
        Vertex left = newRoot.left;
        if (left != null) {
            left.parent = null;
        }
        newRoot.left = null;
        update(newRoot);
        return new VertexPair(left, newRoot);
    }

    static Vertex merge(Vertex left, Vertex right) {
        if (left == null) return right;
        if (right == null) return left;
        while (right.left != null) {
            right = right.left;
        }
        right = splay(right);
        right.left = left;
        update(right);
        return right;
    }

    class Rope {
        Vertex root;

        Rope(String s) {
            if (s == null || s.isEmpty()) {
                this.root = null;
                return;
            }
            this.root = new Vertex(s.charAt(0), 1, null, null, null);
            for (int i = 1; i < s.length(); i++) {
                this.root = merge(this.root, new Vertex(s.charAt(i), 1, null, null, null));
            }
        }

        void process(int i, int j, int k) {
            VertexPair p1 = split(root, i);
            Vertex left = p1.left;
            Vertex middleAndRight = p1.right;

            VertexPair p2 = split(middleAndRight, j - i + 1);
            Vertex middle = p2.left;
            Vertex right = p2.right;

            Vertex tempRoot = merge(left, right);
            
            VertexPair p3 = split(tempRoot, k);
            this.root = merge(merge(p3.left, middle), p3.right);
        }

        String result() {
            StringBuilder sb = new StringBuilder();
            inOrderTraversal(root, sb);
            return sb.toString();
        }

        // Iterative in-order traversal to prevent StackOverflowError on large inputs
        void inOrderTraversal(Vertex node, StringBuilder sb) {
            if (node == null) {
                return;
            }
            Stack<Vertex> stack = new Stack<>();
            Vertex current = node;
            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }
                current = stack.pop();
                sb.append(current.key);
                current = current.right;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new RopeProblem().run();
    }

    public void run() throws IOException {
        FastScanner in = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);
        Rope rope = new Rope(in.next());
        for (int q = in.nextInt(); q > 0; q--) {
            int i = in.nextInt();
            int j = in.nextInt();
            int k = in.nextInt();
            rope.process(i, j, k);
        }
        out.println(rope.result());
        out.close();
    }
}
