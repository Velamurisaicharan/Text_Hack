package pps;

import java.util.*;

public class WeightedEditDistance {

    private final Set<String> dictionary;

    // Costs for different operations
    private final double insertCost;
    private final double deleteCost;
    private final double replaceCost;
    private final double transposeCost;

    public WeightedEditDistance(String text) {

        // Default costs
        this(
                text,
                1.0,   // insertion
                1.0,   // deletion
                2.0,   // replacement
                0.5    // transposition
        );
    }

    public WeightedEditDistance(
            String text,
            double insertCost,
            double deleteCost,
            double replaceCost,
            double transposeCost) {

        this.insertCost = insertCost;
        this.deleteCost = deleteCost;
        this.replaceCost = replaceCost;
        this.transposeCost = transposeCost;

        dictionary = new HashSet<>();

        String[] words =
                text.toLowerCase()
                    .split("[^a-zA-Z]+");

        for (String word : words) {

            if (!word.isEmpty()) {
                dictionary.add(word);
            }
        }

        System.out.println(
                "Dictionary Words : "
                + dictionary.size()
        );
    }

    // =====================================================
    // WEIGHTED DAMERAU-LEVENSHTEIN
    // =====================================================

    public double distance(
            String a,
            String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int n = a.length();
        int m = b.length();

        double[][] dp =
                new double[n + 1][m + 1];

        // Empty string → string
        for (int i = 0; i <= n; i++) {
            dp[i][0] =
                    i * deleteCost;
        }

        // Empty string → string
        for (int j = 0; j <= m; j++) {
            dp[0][j] =
                    j * insertCost;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                double replace;

                if (
                        a.charAt(i - 1)
                        ==
                        b.charAt(j - 1)
                ) {

                    replace =
                            dp[i - 1][j - 1];

                } else {

                    replace =
                            dp[i - 1][j - 1]
                            + replaceCost;
                }

                double insert =
                        dp[i][j - 1]
                        + insertCost;

                double delete =
                        dp[i - 1][j]
                        + deleteCost;

                dp[i][j] =
                        Math.min(
                                replace,
                                Math.min(
                                        insert,
                                        delete
                                )
                        );

                // Adjacent transposition
                if (
                        i > 1
                        && j > 1
                        && a.charAt(i - 1)
                           == b.charAt(j - 2)
                        && a.charAt(i - 2)
                           == b.charAt(j - 1)
                ) {

                    double transpose =
                            dp[i - 2][j - 2]
                            + transposeCost;

                    dp[i][j] =
                            Math.min(
                                    dp[i][j],
                                    transpose
                            );
                }
            }
        }

        return dp[n][m];
    }

    // =====================================================
    // FIND BEST CORRECTIONS
    // =====================================================

    public void suggestCorrections(
            String input,
            int maxSuggestions) {

        input =
                input.toLowerCase().trim();

        if (input.isEmpty()) {

            System.out.println(
                    "Input cannot be empty."
            );

            return;
        }

        if (dictionary.contains(input)) {

            System.out.println(
                    "\nCorrect spelling: "
                    + input
            );

            return;
        }

        List<WordDistance> results =
                new ArrayList<>();

        for (String word : dictionary) {

            /*
             * Ignore words whose lengths are
             * extremely different.
             */

            if (
                    Math.abs(
                            word.length()
                            - input.length()
                    ) > 3
            ) {
                continue;
            }

            double d =
                    distance(
                            input,
                            word
                    );

            results.add(
                    new WordDistance(
                            word,
                            d
                    )
            );
        }

        results.sort(
                Comparator.comparingDouble(
                        x -> x.distance
                )
        );

        System.out.println(
                "\n====== WEIGHTED EDIT DISTANCE ======"
        );

        int count = 0;

        for (WordDistance result :
                results) {

            /*
             * Ignore very distant words.
             */

            if (result.distance > 4.0) {
                break;
            }

            System.out.printf(
                    "%d. %-25s Cost: %.2f%n",
                    count + 1,
                    result.word,
                    result.distance
            );

            count++;

            if (count >= maxSuggestions) {
                break;
            }
        }

        if (count == 0) {

            System.out.println(
                    "No suitable correction found."
            );
        }
    }

    // =====================================================
    // DISPLAY COSTS
    // =====================================================

    public void displayCosts() {

        System.out.println(
                "\n========== ERROR COSTS =========="
        );

        System.out.println(
                "Insertion    : "
                + insertCost
        );

        System.out.println(
                "Deletion     : "
                + deleteCost
        );

        System.out.println(
                "Replacement   : "
                + replaceCost
        );

        System.out.println(
                "Transposition : "
                + transposeCost
        );
    }

    // =====================================================
    // INNER CLASS
    // =====================================================

    private static class WordDistance {

        String word;
        double distance;

        WordDistance(
                String word,
                double distance) {

            this.word = word;
            this.distance = distance;
        }
    }
}