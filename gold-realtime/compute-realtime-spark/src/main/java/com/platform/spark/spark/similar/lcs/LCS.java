package com.platform.spark.spark.similar.lcs;

public class LCS {
    public int findLCS(String A, int n, String B, int m) {
        return core(A, n, B, m);
    }

    public int core(String A, int n, String B, int m) {
        if (n == 0 || m == 0) {
            return 0;
        }

        int[][] dp = new int[n][m];
        // 初始化第0行
        for (int i = 0; i < m; i++) {
            if (A.charAt(0) == B.charAt(i)) {
                for (int j = i; j < m; j++) {
                    dp[0][j] = 1;
                }
                break;
            } else {
                dp[0][i] = 0;
            }
        }

        // 初始化第0列
        for (int i = 0; i < n; i++) {
            if (A.charAt(i) == B.charAt(0)) {
                for (int j = i; j < n; j++) {
                    dp[j][0] = 1;
                }
                break;
            } else {
                dp[i][0] = 0;
            }
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (A.charAt(i) == B.charAt(j)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[n - 1][m - 1];
    }

    public static void main(String[] args) {
        LCS lcs = new LCS();
        String A = "1A2C3";
        int n = A.length();
        String B = "B1D23";
        int m = B.length();
        int res = lcs.findLCS(A, n, B, m);
        System.out.println(res);
    }
}