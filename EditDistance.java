package pps;

import java.util.*;

public class EditDistance {

    private final Set<String> dictionary;

    public EditDistance(String text) {

        dictionary = new HashSet<>();

        /*
         * Extract words from Wikipedia text.
         */
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
    // LEVENSHTEIN DISTANCE
    // =====================================================

    public int levenshtein(
            String a,
            String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int n = a.length();
        int m = b.length();

        int[][] dp =
                new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int cost =
                        a.charAt(i - 1)
                        ==
                        b.charAt(j - 1)
                        ? 0
                        : 1;

                dp[i][j] =
                        Math.min(
                                Math.min(
                                        dp[i - 1][j] + 1,
                                        dp[i][j - 1] + 1
                                ),
                                dp[i - 1][j - 1] + cost
                        );
            }
        }

        return dp[n][m];
    }

    // =====================================================
    // DAMERAU-LEVENSHTEIN DISTANCE
    // =====================================================

    public int damerauLevenshtein(
            String a,
            String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int n = a.length();
        int m = b.length();

        int[][] dp =
                new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int cost =
                        a.charAt(i - 1)
                        ==
                        b.charAt(j - 1)
                        ? 0
                        : 1;

                dp[i][j] =
                        Math.min(
                                Math.min(
                                        dp[i - 1][j] + 1,
                                        dp[i][j - 1] + 1
                                ),
                                dp[i - 1][j - 1]
                                        + cost
                        );

                // Transposition
                if (
                        i > 1
                        && j > 1
                        && a.charAt(i - 1)
                           == b.charAt(j - 2)
                        && a.charAt(i - 2)
                           == b.charAt(j - 1)
                ) {

                    dp[i][j] =
                            Math.min(
                                    dp[i][j],
                                    dp[i - 2][j - 2] + 1
                            );
                }
            }
        }

        return dp[n][m];
    }

    // =====================================================
    // FIND BEST CORRECTION
    // =====================================================

    public void suggestCorrections(
            String input,
            boolean useDamerau) {

        input =
                input.toLowerCase().trim();

        if (input.isEmpty()) {

            System.out.println(
                    "Input cannot be empty."
            );

            return;
        }

        /*
         * If word already exists.
         */

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
             * Ignore words with very different
             * lengths to reduce unnecessary work.
             */

            if (
                    Math.abs(
                            word.length()
                            - input.length()
                    ) > 3
            ) {
                continue;
            }

            int distance;

            if (useDamerau) {

                distance =
                        damerauLevenshtein(
                                input,
                                word
                        );

            } else {

                distance =
                        levenshtein(
                                input,
                                word
                        );
            }

            results.add(
                    new WordDistance(
                            word,
                            distance
                    )
            );
        }

        results.sort(
                Comparator.comparingInt(
                        x -> x.distance
                )
        );

        System.out.println(
                "\n========== SUGGESTIONS =========="
        );

        int count = 0;

        for (WordDistance result :
                results) {

            /*
             * Only display reasonably close words.
             */

            if (result.distance > 3) {
                break;
            }

            System.out.println(
                    (count + 1)
                    + ". "
                    + result.word
                    + "  "
                    + "(Distance: "
                    + result.distance
                    + ")"
            );

            count++;

            if (count == 5) {
                break;
            }
        }

        if (count == 0) {

            System.out.println(
                    "No close correction found."
            );
        }
    }

    // =====================================================
    // WORD + DISTANCE
    // =====================================================

    private static class WordDistance {

        String word;
        int distance;

        WordDistance(
                String word,
                int distance) {

            this.word = word;
            this.distance = distance;
        }
    }
}