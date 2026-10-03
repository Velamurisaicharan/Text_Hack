package pps;

import java.util.*;

public class LCPKasai {

    private final String text;
    private final int[] suffixArray;
    private final int[] rank;
    private final int[] lcp;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LCPKasai(
            String text,
            int[] suffixArray) {

        this.text = text;

        this.suffixArray =
                suffixArray.clone();

        int n = text.length();

        rank = new int[n];

        lcp = new int[n];

        buildRankArray();

        buildLCPArray();
    }

    // =========================================================
    // BUILD RANK ARRAY
    // =========================================================

    private void buildRankArray() {

        for (int i = 0;
             i < suffixArray.length;
             i++) {

            rank[
                    suffixArray[i]
            ] = i;
        }
    }

    // =========================================================
    // KASAI ALGORITHM
    // =========================================================

    private void buildLCPArray() {

        int n = text.length();

        int k = 0;

        /*
         * rank[i] = position of suffix i
         * in the suffix array.
         */

        for (int i = 0;
             i < n;
             i++) {

            int r = rank[i];

            /*
             * Last suffix has no next suffix.
             */

            if (r == n - 1) {

                k = 0;

                continue;
            }

            int j =
                    suffixArray[r + 1];

            /*
             * Compare suffix i and suffix j.
             */

            while (
                    i + k < n
                    &&
                    j + k < n
                    &&
                    text.charAt(i + k)
                    ==
                    text.charAt(j + k)
            ) {

                k++;
            }

            lcp[r] = k;

            /*
             * Kasai optimization.
             */

            if (k > 0) {
                k--;
            }
        }
    }

    // =========================================================
    // DISPLAY LCP ARRAY
    // =========================================================

    public void displayLCP(int limit) {

        System.out.println(
                "\n========== LCP ARRAY =========="
        );

        int count =
                Math.min(
                        limit,
                        lcp.length
                );

        for (int i = 0;
             i < count;
             i++) {

            int suffixIndex =
                    suffixArray[i];

            System.out.println(
                    "SA[" + i + "] = "
                    + suffixIndex
                    + " | LCP = "
                    + lcp[i]
                    + " | "
                    + getPreview(
                            suffixIndex,
                            60
                    )
            );
        }
    }

    // =========================================================
    // FIND REPEATED PHRASES
    // =========================================================

    public void findRepeatedPhrases(
            int minLength,
            int limit) {

        System.out.println(
                "\n========== REPEATED PHRASES =========="
        );

        if (minLength <= 0) {

            System.out.println(
                    "Minimum length must be greater than 0."
            );

            return;
        }

        if (minLength > text.length()) {

            System.out.println(
                    "Minimum length is larger than the text."
            );

            return;
        }

        int found = 0;

        Set<String> printed =
                new HashSet<>();

        /*
         * If LCP[i] >= minLength,
         * suffixArray[i] and suffixArray[i+1]
         * share at least minLength characters.
         */

        for (int i = 0;
             i < lcp.length - 1;
             i++) {

            if (lcp[i] >= minLength) {

                String phrase =
                        text.substring(
                                suffixArray[i],
                                suffixArray[i]
                                        + lcp[i]
                        );

                /*
                 * Avoid printing the same phrase
                 * repeatedly.
                 */

                if (printed.add(phrase)) {

                    System.out.println(
                            "Phrase : \""
                            + phrase
                            + "\""
                    );

                    System.out.println(
                            "Length : "
                            + phrase.length()
                    );

                    System.out.println(
                            "Position 1 : "
                            + suffixArray[i]
                    );

                    System.out.println(
                            "Position 2 : "
                            + suffixArray[i + 1]
                    );

                    System.out.println(
                            "LCP : "
                            + lcp[i]
                    );

                    System.out.println(
                            "--------------------------------"
                    );

                    found++;

                    if (found >= limit) {
                        break;
                    }
                }
            }
        }

        if (found == 0) {

            System.out.println(
                    "No repeated phrases found."
            );
        }
    }

    // =========================================================
    // FIND LONGEST REPEATED PHRASES
    // =========================================================

    public void findLongestRepeatedPhrases(
            int limit) {

        System.out.println(
                "\n====== LONGEST REPEATED PHRASES ======"
        );

        /*
         * Store:
         *
         * phrase -> LCP length
         */

        Map<String, Integer> phrases =
                new HashMap<>();

        for (int i = 0;
             i < lcp.length - 1;
             i++) {

            if (lcp[i] > 0) {

                String phrase =
                        text.substring(
                                suffixArray[i],
                                suffixArray[i]
                                        + lcp[i]
                        );

                phrases.put(
                        phrase,
                        lcp[i]
                );
            }
        }

        /*
         * Sort by phrase length descending.
         */

        List<Map.Entry<String, Integer>>
                list =
                new ArrayList<>(
                        phrases.entrySet()
                );

        list.sort(
                (a, b) ->
                        Integer.compare(
                                b.getValue(),
                                a.getValue()
                        )
        );

        int count = 0;

        for (
                Map.Entry<String, Integer> entry
                        : list
        ) {

            System.out.println(
                    "Phrase : \""
                    + entry.getKey()
                    + "\""
            );

            System.out.println(
                    "Length : "
                    + entry.getValue()
            );

            System.out.println(
                    "--------------------------------"
            );

            count++;

            if (count >= limit) {
                break;
            }
        }

        if (count == 0) {

            System.out.println(
                    "No repeated phrases found."
            );
        }
    }

    // =========================================================
    // GET LCP ARRAY
    // =========================================================

    public int[] getLCPArray() {

        return lcp.clone();
    }

    // =========================================================
    // PREVIEW TEXT
    // =========================================================

    private String getPreview(
            int start,
            int length) {

        if (start >= text.length()) {
            return "";
        }

        int end =
                Math.min(
                        start + length,
                        text.length()
                );

        return text.substring(
                start,
                end
        ).replace(
                "\n",
                " "
        ).replace(
                "\r",
                " "
        );
    }
}