package pps;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TextHack {

    public static void main(String[] args)
            throws Exception {

        Scanner sc = new Scanner(System.in);

        // =====================================================
        // READ WIKIPEDIA DATASET
        // =====================================================

        System.out.println(
                "Reading Wikipedia.txt..."
        );
        

        String text = new String(
                Files.readAllBytes(
                        Paths.get("Wikipedia.txt")
                )
        );
        BitmaskDP bitmaskDP =
                new BitmaskDP(
                        text,
                        10
                );
        text = text
                .replaceAll("\\s+", " ")
                .trim();

        System.out.println(
                "Text loaded successfully."
        );

        System.out.println(
                "Text Size : "
                + text.length()
                + " characters"
        );

        // =====================================================
        // BUILD SA-IS SUFFIX ARRAY
        // =====================================================

        System.out.println(
                "\nBuilding SA-IS Suffix Array..."
        );

        long startTime =
                System.currentTimeMillis();

        SAIS sais = new SAIS(text);

        System.out.println(
                "\nBuilding LCP Array using Kasai Algorithm..."
        );

        long lcpStart =
                System.currentTimeMillis();

        LCPKasai lcpKasai =
                new LCPKasai(
                        text,
                        sais.getSuffixArray()
                );

        long lcpEnd =
                System.currentTimeMillis();

        System.out.println(
                "LCP Array built successfully."
        );

        System.out.println(
                "LCP Construction Time : "
                + (lcpEnd - lcpStart)
                + " ms"
        );

        long endTime =
                System.currentTimeMillis();

        System.out.println(
                "SA-IS Suffix Array built successfully."
        );

        System.out.println(
                "Construction Time : "
                + (endTime - startTime)
                + " ms"
        );
        EditDistance editDistance =
                new EditDistance(text);
        
        WeightedEditDistance weightedEdit =
                new WeightedEditDistance(text);
        

       

        // =====================================================
        // MENU
        // =====================================================

        while (true) {

            System.out.println(
                    "\n========== STRING MATCHING =========="
            );

            System.out.println(
                    "1. Naive Algorithm"
            );

            System.out.println(
                    "2. Rabin-Karp Algorithm"
            );

            System.out.println(
                    "3. KMP Algorithm"
            );

            System.out.println(
                    "4. Z Algorithm"
            );

            System.out.println(
                    "5. SA-IS Suffix Array"
            );
            

            System.out.println(
                    "6. LCP / Kasai - Repeated Phrases"
            );

            System.out.println(
                    "7. Levenshtein / Damerau-Levenshtein"
            );
            System.out.println(
            		"8. Weighted Edit Distance"
            		);
            System.out.println(
            		"9. Bitmask DP - Activity Combinations"
            		);
            System.out.println(
            		"10. Exit"
            		);

            System.out.print(
                    "Enter your choice: "
            );

            int choice = sc.nextInt();

            sc.nextLine();

            switch (choice) {

                // =================================================
                // NAIVE
                // =================================================

                case 1:

                    System.out.print(
                            "Enter Pattern: "
                    );

                    String p1 =
                            sc.nextLine();

                    long naiveStart =
                            System.currentTimeMillis();

                    naive(text, p1);

                    long naiveEnd =
                            System.currentTimeMillis();

                    System.out.println(
                            "Search Time : "
                            + (naiveEnd - naiveStart)
                            + " ms"
                    );

                    break;

                // =================================================
                // RABIN-KARP
                // =================================================

                case 2:

                    System.out.print(
                            "Enter Pattern: "
                    );

                    String p2 =
                            sc.nextLine();

                    long rkStart =
                            System.currentTimeMillis();

                    rabinKarp(text, p2);

                    long rkEnd =
                            System.currentTimeMillis();

                    System.out.println(
                            "Search Time : "
                            + (rkEnd - rkStart)
                            + " ms"
                    );

                    break;

                // =================================================
                // KMP
                // =================================================

                case 3:

                    System.out.print(
                            "Enter Pattern: "
                    );

                    String p3 =
                            sc.nextLine();

                    long kmpStart =
                            System.currentTimeMillis();

                    kmp(text, p3);

                    long kmpEnd =
                            System.currentTimeMillis();

                    System.out.println(
                            "Search Time : "
                            + (kmpEnd - kmpStart)
                            + " ms"
                    );

                    break;

                // =================================================
                // Z ALGORITHM
                // =================================================

                case 4:

                    System.out.print(
                            "Enter Pattern: "
                    );

                    String p4 =
                            sc.nextLine();

                    long zStart =
                            System.currentTimeMillis();

                    zAlgorithm(text, p4);

                    long zEnd =
                            System.currentTimeMillis();

                    System.out.println(
                            "Search Time : "
                            + (zEnd - zStart)
                            + " ms"
                    );

                    break;

                // =================================================
                // SA-IS
                // =================================================

                case 5:
                    System.out.print("Enter Pattern: ");

                    String p5 = sc.nextLine();

                    long saisStart =
                            System.currentTimeMillis();

                    sais.search(p5);

                    long saisEnd =
                            System.currentTimeMillis();

                    System.out.println(
                            "Search Time : "
                            + (saisEnd - saisStart)
                            + " ms"
                    );

                    break;
                case 6:

                    System.out.print(
                            "Enter minimum phrase length: "
                    );

                    int minLength =
                            sc.nextInt();

                    System.out.print(
                            "Enter maximum number of phrases to display: "
                    );

                    int limit =
                            sc.nextInt();

                    long repeatedStart =
                            System.currentTimeMillis();

                    lcpKasai.findRepeatedPhrases(
                            minLength,
                            limit
                    );

                    long repeatedEnd =
                            System.currentTimeMillis();

                    System.out.println(
                            "Processing Time : "
                            + (repeatedEnd - repeatedStart)
                            + " ms"
                    );

                    break;
                    
                    
                case 7:

                    System.out.print(
                            "Enter word to correct: "
                    );

                    String input =
                            sc.nextLine();

                    System.out.println(
                            "\n1. Levenshtein"
                    );

                    System.out.println(
                            "2. Damerau-Levenshtein"
                    );

                    System.out.print(
                            "Choose algorithm: "
                    );

                    int editChoice =
                            sc.nextInt();

                    sc.nextLine();

                    if (editChoice == 1) {

                        editDistance.suggestCorrections(
                                input,
                                false
                        );

                    } else if (editChoice == 2) {

                        editDistance.suggestCorrections(
                                input,
                                true
                        );

                    } else {

                        System.out.println(
                                "Invalid choice."
                        );
                    }

                    break;
                    
                case 8:

                    System.out.print(
                            "Enter word to correct: "
                    );

                    String weightedInput =
                            sc.nextLine();

                    System.out.print(
                            "Enter maximum suggestions: "
                    );

                    int weightedLimit =
                            sc.nextInt();

                    sc.nextLine();

                    weightedEdit.displayCosts();

                    long weightedStart =
                            System.currentTimeMillis();

                    weightedEdit.suggestCorrections(
                            weightedInput,
                            weightedLimit
                    );

                    long weightedEnd =
                            System.currentTimeMillis();

                    System.out.println(
                            "Processing Time : "
                            + (weightedEnd - weightedStart)
                            + " ms"
                    );

                    break;
                    
                case 9:

                    System.out.println(
                            "\n========== BITMASK DP =========="
                    );

                    System.out.println(
                            "1. Display Wikipedia Words"
                    );

                    System.out.println(
                            "2. Display All Combinations"
                    );

                    System.out.println(
                            "3. Find Best Combination"
                    );

                    System.out.println(
                            "4. Best Combination with Word Limit"
                    );

                    System.out.print(
                            "Enter choice: "
                    );

                    int bitChoice =
                            sc.nextInt();

                    if (bitChoice == 1) {

                        bitmaskDP.displayWords();

                    } else if (bitChoice == 2) {

                        bitmaskDP.displayAllCombinations();

                    } else if (bitChoice == 3) {

                        bitmaskDP.findBestCombination();

                    } else if (bitChoice == 4) {

                        System.out.print(
                                "Enter maximum number of words: "
                        );

                        int maxWords =
                                sc.nextInt();

                        bitmaskDP.findBestCombination(
                                maxWords
                        );

                    } else {

                        System.out.println(
                                "Invalid choice."
                        );
                    }

                    break;
                    
                // =================================================
                // EXIT
                // =================================================

                case 10:

                    System.out.println(
                            "\nProgram Exited Successfully."
                    );

                    sc.close();

                    return;

                // =================================================
                // INVALID
                // =================================================

                default:

                    System.out.println(
                            "Invalid Choice!"
                    );
            }
        }
    }

    // =========================================================
    // NAIVE STRING MATCHING
    // =========================================================

    static void naive(
            String text,
            String pattern) {

        int n = text.length();

        int m = pattern.length();

        if (m == 0) {

            System.out.println(
                    "Pattern cannot be empty."
            );

            return;
        }

        if (m > n) {

            System.out.println(
                    "Pattern Not Found"
            );

            return;
        }

        boolean found = false;

        for (int i = 0;
             i <= n - m;
             i++) {

            int j;

            for (j = 0;
                 j < m;
                 j++) {

                if (
                        text.charAt(i + j)
                        != pattern.charAt(j)
                ) {

                    break;
                }
            }

            if (j == m) {

                System.out.println(
                        "Pattern Found at Index : "
                        + i
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "Pattern Not Found"
            );
        }
    }

    // =========================================================
    // RABIN-KARP
    // =========================================================

    static void rabinKarp(
            String text,
            String pattern) {

        int n = text.length();

        int m = pattern.length();

        if (m == 0) {

            System.out.println(
                    "Pattern cannot be empty."
            );

            return;
        }

        if (m > n) {

            System.out.println(
                    "Pattern Not Found"
            );

            return;
        }

        int patternHash = 0;

        int textHash = 0;

        for (int i = 0; i < m; i++) {

            patternHash +=
                    pattern.charAt(i);

            textHash +=
                    text.charAt(i);
        }

        boolean found = false;

        for (int i = 0;
             i <= n - m;
             i++) {

            if (
                    patternHash
                    == textHash
            ) {

                int j;

                for (j = 0;
                     j < m;
                     j++) {

                    if (
                            text.charAt(i + j)
                            != pattern.charAt(j)
                    ) {

                        break;
                    }
                }

                if (j == m) {

                    System.out.println(
                            "Pattern Found at Index : "
                            + i
                    );

                    found = true;
                }
            }

            if (i < n - m) {

                textHash =
                        textHash
                        - text.charAt(i)
                        + text.charAt(i + m);
            }
        }

        if (!found) {

            System.out.println(
                    "Pattern Not Found"
            );
        }
    }

    // =========================================================
    // KMP
    // =========================================================

    static void kmp(
            String text,
            String pattern) {

        int n = text.length();

        int m = pattern.length();

        if (m == 0) {

            System.out.println(
                    "Pattern cannot be empty."
            );

            return;
        }

        if (m > n) {

            System.out.println(
                    "Pattern Not Found"
            );

            return;
        }

        int[] lps = new int[m];

        int len = 0;

        int i = 1;

        while (i < m) {

            if (
                    pattern.charAt(i)
                    == pattern.charAt(len)
            ) {

                len++;

                lps[i] = len;

                i++;

            } else {

                if (len != 0) {

                    len =
                            lps[len - 1];

                } else {

                    lps[i] = 0;

                    i++;
                }
            }
        }

        i = 0;

        int j = 0;

        boolean found = false;

        while (i < n) {

            if (
                    text.charAt(i)
                    == pattern.charAt(j)
            ) {

                i++;

                j++;
            }

            if (j == m) {

                System.out.println(
                        "Pattern Found at Index : "
                        + (i - j)
                );

                found = true;

                j =
                        lps[j - 1];

            } else if (
                    i < n
                    &&
                    text.charAt(i)
                    != pattern.charAt(j)
            ) {

                if (j != 0) {

                    j =
                            lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        if (!found) {

            System.out.println(
                    "Pattern Not Found"
            );
        }
    }

    // =========================================================
    // Z ALGORITHM
    // =========================================================

    static void zAlgorithm(
            String text,
            String pattern) {

        if (pattern.isEmpty()) {

            System.out.println(
                    "Pattern cannot be empty."
            );

            return;
        }

        String concat =
                pattern + "$" + text;

        int n =
                concat.length();

        int[] z =
                new int[n];

        int left = 0;

        int right = 0;

        for (int i = 1;
             i < n;
             i++) {

            if (i <= right) {

                z[i] =
                        Math.min(
                                right - i + 1,
                                z[i - left]
                        );
            }

            while (
                    i + z[i] < n
                    &&
                    concat.charAt(z[i])
                    ==
                    concat.charAt(
                            i + z[i]
                    )
            ) {

                z[i]++;
            }

            if (
                    i + z[i] - 1
                    > right
            ) {

                left = i;

                right =
                        i + z[i] - 1;
            }
        }

        boolean found = false;

        for (int i = 0;
             i < n;
             i++) {

            if (
                    z[i]
                    == pattern.length()
            ) {

                System.out.println(
                        "Pattern Found at Index : "
                        + (
                            i
                            - pattern.length()
                            - 1
                        )
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "Pattern Not Found"
            );
        }
    }
}