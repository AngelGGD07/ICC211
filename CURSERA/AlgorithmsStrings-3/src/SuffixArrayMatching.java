import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuffixArrayMatching {
    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
        }
    }


    public int[] computeSuffixArray(String text) {
        int[] order = sortCharacters(text);
        int[] classArray = computeCharClasses(text, order);
        int L = 1;
        while (L < text.length()) {
            order = sortDoubled(text, L, order, classArray);
            classArray = updateClasses(order, classArray, L);
            L = 2 * L;
        }
        return order;
    }

    private int[] sortCharacters(String S) {
        int[] order = new int[S.length()];
        int[] count = new int[5]; // $, A, C, G, T
        for (int i = 0; i < S.length(); i++) {
            count[charToIndex(S.charAt(i))]++;
        }
        for (int j = 1; j < 5; j++) {
            count[j] = count[j] + count[j - 1];
        }
        for (int i = S.length() - 1; i >= 0; i--) {
            int c = charToIndex(S.charAt(i));
            count[c]--;
            order[count[c]] = i;
        }
        return order;
    }

    private int charToIndex(char c) {
        switch (c) {
            case '$': return 0;
            case 'A': return 1;
            case 'C': return 2;
            case 'G': return 3;
            case 'T': return 4;
            default: return -1;
        }
    }

    private int[] computeCharClasses(String S, int[] order) {
        int[] classArray = new int[S.length()];
        classArray[order[0]] = 0;
        for (int i = 1; i < S.length(); i++) {
            if (S.charAt(order[i]) != S.charAt(order[i - 1])) {
                classArray[order[i]] = classArray[order[i - 1]] + 1;
            } else {
                classArray[order[i]] = classArray[order[i - 1]];
            }
        }
        return classArray;
    }

    private int[] sortDoubled(String S, int L, int[] order, int[] classArray) {
        int[] count = new int[S.length()];
        int[] newOrder = new int[S.length()];
        for (int i = 0; i < S.length(); i++) {
            count[classArray[i]]++;
        }
        for (int j = 1; j < S.length(); j++) {
            count[j] = count[j] + count[j - 1];
        }
        for (int i = S.length() - 1; i >= 0; i--) {
            int start = (order[i] - L + S.length()) % S.length();
            int cl = classArray[start];
            count[cl]--;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    private int[] updateClasses(int[] newOrder, int[] classArray, int L) {
        int n = newOrder.length;
        int[] newClassArray = new int[n];
        newClassArray[newOrder[0]] = 0;
        for (int i = 1; i < n; i++) {
            int cur = newOrder[i];
            int prev = newOrder[i - 1];
            int mid = (cur + L) % n;
            int midPrev = (prev + L) % n;
            if (classArray[cur] != classArray[prev] || classArray[mid] != classArray[midPrev]) {
                newClassArray[cur] = newClassArray[prev] + 1;
            } else {
                newClassArray[cur] = newClassArray[prev];
            }
        }
        return newClassArray;
    }

    public List<Integer> findOccurrences(String pattern, String text, int[] suffixArray) {
        List<Integer> result = new ArrayList<>();
        int minIndex = 0;
        int maxIndex = text.length();
        int l = 0;
        int r = text.length();
        while (l < r) {
            int mid = (l + r) / 2;
            if (compare(pattern, text, suffixArray[mid]) > 0) {
                minIndex = mid + 1;
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        int start = l;
        r = text.length();
        while (l < r) {
            int mid = (l + r) / 2;
            if (compare(pattern, text, suffixArray[mid]) < 0) {
                maxIndex = mid;
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        int end = r;
        if (start > end) {
            return result;
        }
        for (int i = start; i < end; i++) {
            result.add(suffixArray[i]);
        }
        return result;
    }

    private int compare(String pattern, String text, int suffix) {
        int length = Math.min(pattern.length(), text.length() - suffix);
        for (int i = 0; i < length; i++) {
            if (pattern.charAt(i) < text.charAt(suffix + i)) {
                return -1;
            } else if (pattern.charAt(i) > text.charAt(suffix + i)) {
                return 1;
            }
        }
        if (pattern.length() > length) {
            return 1;
        }
        return 0;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {
        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrences(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
    }
}
