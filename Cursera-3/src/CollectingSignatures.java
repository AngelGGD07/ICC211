import java.util.Arrays;
import java.util.Scanner;

public class CollectingSignatures {

    private static int[] optimalPoints(Segment[] segments) {
        Arrays.sort(segments, (a, b) -> Integer.compare(a.end, b.end));
        int[] points = new int[segments.length];
        int point = segments[0].end;
        points[0] = point;
        int count = 1;
        for (int i = 1; i < segments.length; i++) {
            if (point < segments[i].start || point > segments[i].end) {
                point = segments[i].end;
                points[count] = point;
                count++;
            }
        }
        int[] result = new int[count];
        System.arraycopy(points, 0, result, 0, count);
        return result;
    }

    private static class Segment {
        int start, end;

        Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int start, end;
            start = scanner.nextInt();
            end = scanner.nextInt();
            segments[i] = new Segment(start, end);
        }
        int[] points = optimalPoints(segments);
        System.out.println(points.length);
        for (int point : points) {
            System.out.print(point + " ");
        }
    }
}
