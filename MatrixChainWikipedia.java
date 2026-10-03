package pps;

public class MatrixChainWikipedia {

    private final String text;

    private final String[] operations;

    private final int[] dimensions;

    private int[][] dp;

    private int[][] split;

    public MatrixChainWikipedia(String text) {

        this.text = text;

        /*
         * These represent stages in the
         * Wikipedia text-processing pipeline.
         */
        operations = new String[] {
                "Tokenization",
                "Frequency Analysis",
                "SA-IS Indexing",
                "LCP Analysis",
                "Pattern Search"
        };

        /*
         * Number of rows/columns used by each
         * transformation.
         *
         * We derive the first dimension from
         * Wikipedia.txt.
         */
        int wordCount =
                countWords(text);

        /*
         * Keep dimensions manageable.
         */
        int n =
                Math.max(
                        10,
                        Math.min(
                                wordCount,
                                1000
                        )
                );

        /*
         *
         * A1 =  n × 20
         * A2 = 20 × 30
         * A3 = 30 × 15
         * A4 = 15 × 25
         * A5 = 25 × 10
         *
         * The first dimension is related to
         * the size of the Wikipedia data.
         */
        dimensions = new int[] {
                n,
                20,
                30,
                15,
                25,
                10
        };
    }

    // =====================================================
    // COUNT WORDS
    // =====================================================

    private int countWords(String text) {

        String[] words =
                text.split("[^a-zA-Z]+");

        int count = 0;

        for (String word : words) {

            if (!word.isEmpty()) {
                count++;
            }
        }

        return count;
    }

    // =====================================================
    // MATRIX CHAIN DP
    // =====================================================

    public void optimize() {

        int n =
                dimensions.length - 1;

        dp =
                new int[n][n];

        split =
                new int[n][n];

        /*
         * dp[i][j] =
         * minimum multiplication cost
         * for matrices i through j.
         */

        for (int length = 2;
             length <= n;
             length++) {

            for (int i = 0;
                 i <= n - length;
                 i++) {

                int j =
                        i + length - 1;

                dp[i][j] =
                        Integer.MAX_VALUE;

                for (int k = i;
                     k < j;
                     k++) {

                    int cost =
                            dp[i][k]
                            + dp[k + 1][j]
                            + dimensions[i]
                            * dimensions[k + 1]
                            * dimensions[j + 1];

                    if (cost < dp[i][j]) {

                        dp[i][j] = cost;

                        split[i][j] = k;
                    }
                }
            }
        }
    }

    // =====================================================
    // DISPLAY MATRICES
    // =====================================================

    public void displayOperations() {

        System.out.println(
                "\n========== WIKIPEDIA DATA PIPELINE =========="
        );

        for (int i = 0;
             i < operations.length;
             i++) {

            System.out.println(
                    "A"
                    + (i + 1)
                    + " : "
                    + operations[i]
                    + " ["
                    + dimensions[i]
                    + " x "
                    + dimensions[i + 1]
                    + "]"
            );
        }
    }

    // =====================================================
    // DISPLAY OPTIMAL COST
    // =====================================================

    public void displayOptimalCost() {

        int n =
                operations.length;

        System.out.println(
                "\n========== MATRIX-CHAIN RESULT =========="
        );

        System.out.println(
                "Minimum Scalar Multiplications : "
                + dp[0][n - 1]
        );

        System.out.println(
                "\nOptimal Processing Order:"
        );

        printOptimalOrder(
                0,
                n - 1
        );

        System.out.println();
    }

    // =====================================================
    // PRINT OPTIMAL PARENTHESIZATION
    // =====================================================

    private void printOptimalOrder(
            int i,
            int j) {

        if (i == j) {

            System.out.print(
                    "A"
                    + (i + 1)
            );

            return;
        }

        System.out.print("(");

        printOptimalOrder(
                i,
                split[i][j]
        );

        System.out.print(" × ");

        printOptimalOrder(
                split[i][j] + 1,
                j
        );

        System.out.print(")");
    }

    // =====================================================
    // DISPLAY DP TABLE
    // =====================================================

    public void displayDPTable() {

        int n =
                operations.length;

        System.out.println(
                "\n========== DP TABLE =========="
        );

        for (int i = 0;
             i < n;
             i++) {

            for (int j = 0;
                 j < n;
                 j++) {

                System.out.printf(
                        "%8d",
                        dp[i][j]
                );
            }

            System.out.println();
        }
    }
}