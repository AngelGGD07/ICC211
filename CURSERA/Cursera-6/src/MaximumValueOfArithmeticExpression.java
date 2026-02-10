import java.util.Scanner;

public class MaximumValueOfArithmeticExpression {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.next();
        System.out.println(getMaxValue(expression));
    }

    private static long getMaxValue(String expression) {
        int numberOfOperands = (expression.length() + 1) / 2;
        long[][] minValues = new long[numberOfOperands][numberOfOperands];
        long[][] maxValues = new long[numberOfOperands][numberOfOperands];

        for (int i = 0; i < numberOfOperands; i++) {
            minValues[i][i] = expression.charAt(2 * i) - '0';
            maxValues[i][i] = expression.charAt(2 * i) - '0';
        }

        for (int subExpressionLength = 1; subExpressionLength < numberOfOperands; subExpressionLength++) {
            for (int startIndex = 0; startIndex < numberOfOperands - subExpressionLength; startIndex++) {
                int endIndex = startIndex + subExpressionLength;
                long[] minMax = getMinAndMax(expression, startIndex, endIndex, minValues, maxValues);
                minValues[startIndex][endIndex] = minMax[0];
                maxValues[startIndex][endIndex] = minMax[1];
            }
        }

        return maxValues[0][numberOfOperands - 1];
    }

    private static long[] getMinAndMax(String expression, int startIndex, int endIndex, long[][] minValues, long[][] maxValues) {
        long minResult = Long.MAX_VALUE;
        long maxResult = Long.MIN_VALUE;

        for (int splitIndex = startIndex; splitIndex < endIndex; splitIndex++) {
            char operator = expression.charAt(2 * splitIndex + 1);
            long eval1 = eval(maxValues[startIndex][splitIndex], maxValues[splitIndex + 1][endIndex], operator);
            long eval2 = eval(maxValues[startIndex][splitIndex], minValues[splitIndex + 1][endIndex], operator);
            long eval3 = eval(minValues[startIndex][splitIndex], maxValues[splitIndex + 1][endIndex], operator);
            long eval4 = eval(minValues[startIndex][splitIndex], minValues[splitIndex + 1][endIndex], operator);
            minResult = Math.min(minResult, Math.min(eval1, Math.min(eval2, Math.min(eval3, eval4))));
            maxResult = Math.max(maxResult, Math.max(eval1, Math.max(eval2, Math.max(eval3, eval4))));
        }

        return new long[]{minResult, maxResult};
    }

    private static long eval(long operand1, long operand2, char operator) {
        if (operator == '+') {
            return operand1 + operand2;
        } else if (operator == '-') {
            return operand1 - operand2;
        } else if (operator == '*') {
            return operand1 * operand2;
        } else {
            // Should not happen for valid input
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
