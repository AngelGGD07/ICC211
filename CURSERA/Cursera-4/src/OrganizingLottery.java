import java.io.*;
import java.util.*;

public class OrganizingLottery {

    private static int[] fastCountSegments(int[] starts, int[] ends, int[] points) {
        int[] cnt = new int[points.length];
        Arrays.sort(starts);
        Arrays.sort(ends);
        for(int i=0; i<points.length; i++){
            int l = countLessOrEqual(starts, points[i]);
            int r = countLess(ends, points[i]);
            cnt[i] = l - r;
        }
        return cnt;
    }

    private static int countLessOrEqual(int[] a, int p){
        int l = 0, r = a.length - 1;
        int count = 0;
        while(l <= r){
            int mid = l + (r - l) / 2;
            if(a[mid] <= p){
                count = mid + 1;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return count;
    }

    private static int countLess(int[] a, int p){
        int l = 0, r = a.length - 1;
        int count = 0;
        while(l <= r){
            int mid = l + (r - l) / 2;
            if(a[mid] < p){
                count = mid + 1;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return count;
    }

    private static int[] naiveCountSegments(int[] starts, int[] ends, int[] points) {
        int[] cnt = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < starts.length; j++) {
                if (starts[j] <= points[i] && points[i] <= ends[j]) {
                    cnt[i]++;
                }
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int s = scanner.nextInt();
        int p = scanner.nextInt();
        int[] starts = new int[s];
        int[] ends = new int[s];
        for (int i = 0; i < s; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }
        int[] points = new int[p];
        for (int i = 0; i < p; i++) {
            points[i] = scanner.nextInt();
        }
        
        int[] cnt = fastCountSegments(starts, ends, points);
        for (int x : cnt) {
            System.out.print(x + " ");
        }
    }
    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}