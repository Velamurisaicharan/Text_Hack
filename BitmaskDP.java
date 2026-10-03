package pps;

import java.util.*;

public class BitmaskDP {

    private final String[] words;
    private final int[] frequency;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BitmaskDP(String text, int numberOfWords) {

        /*
         * Count word frequencies from Wikipedia.txt
         */
        Map<String, Integer> frequencyMap =
                new HashMap<>();

        String[] tokens =
                text.toLowerCase()
                    .split("[^a-zA-Z]+");

        for (String word : tokens) {

            if (word.length() < 3) {
                continue;
            }

            frequencyMap.put(
                    word,
                    frequencyMap.getOrDefault(
                            word,
                            0
                    ) + 1
            );
        }

        /*
         * Sort words by frequency.
         */
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(
                        frequencyMap.entrySet()
                );

        list.sort(
                (a, b) ->
                        Integer.compare(
                                b.getValue(),
                                a.getValue()
                        )
        );

        /*
         * Keep only the top N words.
         *
         * Bitmask uses 2^N combinations,
         * so N should be small.
         */
        int n =
                Math.min(
                        numberOfWords,
                        list.size()
                );

        words = new String[n];
        frequency = new int[n];

        for (int i = 0; i < n; i++) {

            words[i] =
                    list.get(i).getKey();

            frequency[i] =
                    list.get(i).getValue();
        }

        System.out.println(
                "\nBitmask Dataset Created"
        );

        System.out.println(
                "Words selected : " + n
        );
    }

    // =====================================================
    // DISPLAY WORDS AND FREQUENCIES
    // =====================================================

    public void displayWords() {

        System.out.println(
                "\n========== WIKIPEDIA WORDS =========="
        );

        for (int i = 0;
             i < words.length;
             i++) {

            System.out.println(
                    i
                    + " -> "
                    + words[i]
                    + " | Frequency = "
                    + frequency[i]
            );
        }
    }

    // =====================================================
    // DISPLAY ALL BITMASK COMBINATIONS
    // =====================================================

    public void displayAllCombinations() {

        int n = words.length;

        if (n > 20) {

            System.out.println(
                    "Too many words to display all combinations."
            );

            return;
        }

        int totalMasks =
                1 << n;

        System.out.println(
                "\n========== BITMASK COMBINATIONS =========="
        );

        for (int mask = 1;
             mask < totalMasks;
             mask++) {

            int score = 0;

            System.out.print(
                    "Mask "
                    + String.format(
                            "%" + n + "s",
                            Integer.toBinaryString(mask)
                    ).replace(
                            ' ',
                            '0'
                    )
                    + " -> "
            );

            for (int i = 0;
                 i < n;
                 i++) {

                if (
                        (mask & (1 << i))
                        != 0
                ) {

                    System.out.print(
                            words[i]
                            + " "
                    );

                    score += frequency[i];
                }
            }

            System.out.println(
                    "| Score = " + score
            );
        }
    }

    // =====================================================
    // FIND BEST COMBINATION
    // =====================================================

    public void findBestCombination() {

        int n = words.length;

        if (n > 20) {

            System.out.println(
                    "Too many words for direct bitmask enumeration."
            );

            return;
        }

        int totalMasks =
                1 << n;

        int bestMask = 0;

        int bestScore = 0;

        for (int mask = 1;
             mask < totalMasks;
             mask++) {

            int score = 0;

            for (int i = 0;
                 i < n;
                 i++) {

                if (
                        (mask & (1 << i))
                        != 0
                ) {

                    score += frequency[i];
                }
            }

            if (score > bestScore) {

                bestScore = score;

                bestMask = mask;
            }
        }

        System.out.println(
                "\n========== BEST COMBINATION =========="
        );

        System.out.println(
                "Mask : "
                + String.format(
                        "%" + n + "s",
                        Integer.toBinaryString(
                                bestMask
                        )
                ).replace(
                        ' ',
                        '0'
                )
        );

        System.out.println(
                "Total Frequency : "
                + bestScore
        );

        System.out.print(
                "Selected Words : "
        );

        for (int i = 0;
             i < n;
             i++) {

            if (
                    (bestMask & (1 << i))
                    != 0
            ) {

                System.out.print(
                        words[i]
                        + " "
                );
            }
        }

        System.out.println();
    }

    // =====================================================
    // FIND COMBINATIONS WITHIN A LIMIT
    // =====================================================

    public void findBestCombination(
            int maximumWords) {

        int n = words.length;

        if (n > 20) {

            System.out.println(
                    "Too many words for bitmask enumeration."
            );

            return;
        }

        int totalMasks =
                1 << n;

        int bestMask = 0;

        int bestScore = 0;

        for (int mask = 1;
             mask < totalMasks;
             mask++) {

            int selectedCount = 0;

            int score = 0;

            for (int i = 0;
                 i < n;
                 i++) {

                if (
                        (mask & (1 << i))
                        != 0
                ) {

                    selectedCount++;

                    score += frequency[i];
                }
            }

            /*
             * Only allow combinations
             * containing maximumWords words.
             */
            if (
                    selectedCount <= maximumWords
                    &&
                    score > bestScore
            ) {

                bestScore = score;

                bestMask = mask;
            }
        }

        System.out.println(
                "\n========== BEST LIMITED COMBINATION =========="
        );

        System.out.println(
                "Maximum Words : "
                + maximumWords
        );

        System.out.println(
                "Total Frequency : "
                + bestScore
        );

        System.out.print(
                "Selected Words : "
        );

        for (int i = 0;
             i < n;
             i++) {

            if (
                    (bestMask & (1 << i))
                    != 0
            ) {

                System.out.print(
                        words[i]
                        + " "
                );
            }
        }

        System.out.println();
    }
}