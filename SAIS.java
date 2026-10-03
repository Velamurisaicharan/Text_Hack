package pps;

import java.util.Arrays;

public class SAIS {

    private final String text;
    private final int[] suffixArray;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SAIS(String text) {

        if (text == null) {
            throw new IllegalArgumentException(
                    "Text cannot be null"
            );
        }

        this.text = text;

        suffixArray = buildSuffixArray(text);
    }

    // =========================================================
    // BUILD SUFFIX ARRAY
    // =========================================================

    private int[] buildSuffixArray(String text) {

        int n = text.length();

        /*
         * Add a sentinel character 0.
         *
         * Every real character is shifted by +1.
         *
         * Therefore:
         *
         * sentinel = 0
         * real characters = 1 ... 65536
         */

        int[] s = new int[n + 1];

        for (int i = 0; i < n; i++) {
            s[i] = text.charAt(i) + 1;
        }

        s[n] = 0;

        int upper = Character.MAX_VALUE + 1;

        int[] fullSA = sais(s, upper);

        /*
         * Remove the sentinel suffix.
         */

        int[] result = new int[n];

        int j = 0;

        for (int value : fullSA) {

            if (value != n) {
                result[j++] = value;
            }
        }

        return result;
    }

    // =========================================================
    // SA-IS
    // =========================================================

    private int[] sais(
            int[] s,
            int upper) {

        int n = s.length;

        // -----------------------------------------------------
        // SMALL CASES
        // -----------------------------------------------------

        if (n == 0) {
            return new int[0];
        }

        if (n == 1) {
            return new int[]{0};
        }

        if (n == 2) {

            if (s[0] < s[1]) {
                return new int[]{0, 1};
            }

            return new int[]{1, 0};
        }

        // -----------------------------------------------------
        // 1. CLASSIFY L-TYPE / S-TYPE
        // -----------------------------------------------------

        boolean[] ls = new boolean[n];

        /*
         * true  = S-type
         * false = L-type
         */

        for (int i = n - 2; i >= 0; i--) {

            if (s[i] == s[i + 1]) {

                ls[i] = ls[i + 1];

            } else {

                ls[i] = s[i] < s[i + 1];
            }
        }

        // -----------------------------------------------------
        // 2. CREATE BUCKET INFORMATION
        // -----------------------------------------------------

        int[] sumL =
                new int[upper + 1];

        int[] sumS =
                new int[upper + 1];

        for (int i = 0; i < n; i++) {

            if (ls[i]) {

                sumL[s[i] + 1]++;

            } else {

                sumS[s[i]]++;
            }
        }

        /*
         * Convert counts to bucket boundaries.
         */

        for (int i = 0; i < upper; i++) {

            sumS[i] += sumL[i];

            sumL[i + 1] += sumS[i];
        }

        sumS[upper] += sumL[upper];

        // -----------------------------------------------------
        // 3. INDUCED SORTING
        // -----------------------------------------------------

        int[] sa =
                new int[n];

        /*
         * This is the critical part.
         *
         * It follows the standard SA-IS induced sorting
         * procedure.
         */

        induce(
                s,
                sa,
                ls,
                sumL,
                sumS,
                new int[0]
        );

        // -----------------------------------------------------
        // 4. FIND LMS POSITIONS
        // -----------------------------------------------------

        int[] lmsMap =
                new int[n + 1];

        Arrays.fill(
                lmsMap,
                -1
        );

        int m = 0;

        for (int i = 1; i < n; i++) {

            if (
                    !ls[i - 1]
                    &&
                    ls[i]
            ) {

                lmsMap[i] = m++;
            }
        }

        int[] lms =
                new int[m];

        int p = 0;

        for (int i = 1; i < n; i++) {

            if (
                    !ls[i - 1]
                    &&
                    ls[i]
            ) {

                lms[p++] = i;
            }
        }

        // -----------------------------------------------------
        // 5. INITIAL LMS INDUCED SORT
        // -----------------------------------------------------

        induce(
                s,
                sa,
                ls,
                sumL,
                sumS,
                lms
        );

        // -----------------------------------------------------
        // 6. REDUCE LMS SUBSTRINGS
        // -----------------------------------------------------

        if (m > 0) {

            int[] sortedLms =
                    new int[m];

            int sortedCount = 0;

            for (int v : sa) {

                if (
                        v >= 0
                        &&
                        lmsMap[v] != -1
                ) {

                    sortedLms[
                            sortedCount++
                    ] = v;
                }
            }

            int[] recS =
                    new int[m];

            int recUpper = 0;

            recS[
                    lmsMap[
                            sortedLms[0]
                    ]
            ] = 0;

            // -------------------------------------------------
            // NAME LMS SUBSTRINGS
            // -------------------------------------------------

            for (int i = 1; i < m; i++) {

                int l =
                        sortedLms[i - 1];

                int r =
                        sortedLms[i];

                int endL =
                        (
                                lmsMap[l] + 1 < m
                        )
                        ?
                        lms[
                                lmsMap[l] + 1
                        ]
                        :
                        n;

                int endR =
                        (
                                lmsMap[r] + 1 < m
                        )
                        ?
                        lms[
                                lmsMap[r] + 1
                        ]
                        :
                        n;

                boolean same = true;

                /*
                 * Different lengths means different
                 * LMS substrings.
                 */

                if (
                        endL - l
                        !=
                        endR - r
                ) {

                    same = false;

                } else {

                    /*
                     * Compare the two LMS substrings.
                     */

                    while (
                            l < endL
                            &&
                            s[l] == s[r]
                    ) {

                        l++;
                        r++;
                    }

                    if (
                            l == n
                            ||
                            s[l] != s[r]
                    ) {

                        same = false;
                    }
                }

                if (!same) {
                    recUpper++;
                }

                recS[
                        lmsMap[
                                sortedLms[i]
                        ]
                ] = recUpper;
            }

            // -------------------------------------------------
            // 7. RECURSIVE SA-IS
            // -------------------------------------------------

            int[] recSA =
                    sais(
                            recS,
                            recUpper
                    );

            // -------------------------------------------------
            // 8. RECONSTRUCT LMS ORDER
            // -------------------------------------------------

            for (int i = 0;
                 i < m;
                 i++) {

                sortedLms[i] =
                        lms[
                                recSA[i]
                        ];
            }

            // -------------------------------------------------
            // 9. FINAL INDUCED SORT
            // -------------------------------------------------

            induce(
                    s,
                    sa,
                    ls,
                    sumL,
                    sumS,
                    sortedLms
            );
        }

        return sa;
    }

    // =========================================================
    // INDUCED SORTING
    // =========================================================

    private void induce(
            int[] s,
            int[] sa,
            boolean[] ls,
            int[] sumL,
            int[] sumS,
            int[] lms) {

        int n = s.length;

        Arrays.fill(
                sa,
                -1
        );

        // -----------------------------------------------------
        // PLACE LMS SUFFIXES
        // -----------------------------------------------------

        int[] buf =
                sumS.clone();

        for (int d : lms) {

            if (d == n) {
                continue;
            }

            sa[
                    buf[s[d]]++
            ] = d;
        }

        // -----------------------------------------------------
        // INDUCE L-TYPE SUFFIXES
        // -----------------------------------------------------

        System.arraycopy(
                sumL,
                0,
                buf,
                0,
                sumL.length
        );

        /*
         * The last suffix n-1 is always placed first.
         */

        sa[
                buf[s[n - 1]]++
        ] = n - 1;

        /*
         * Scan from left to right.
         */

        for (int i = 0;
             i < n;
             i++) {

            int v =
                    sa[i];

            if (
                    v >= 1
                    &&
                    !ls[v - 1]
            ) {

                sa[
                        buf[
                                s[v - 1]
                        ]++
                ] = v - 1;
            }
        }

        // -----------------------------------------------------
        // INDUCE S-TYPE SUFFIXES
        // -----------------------------------------------------

        System.arraycopy(
                sumL,
                0,
                buf,
                0,
                sumL.length
        );

        /*
         * Scan from right to left.
         */

        for (int i = n - 1;
             i >= 0;
             i--) {

            int v =
                    sa[i];

            if (
                    v >= 1
                    &&
                    ls[v - 1]
            ) {

                sa[
                        --buf[
                                s[v - 1] + 1
                        ]
                ] = v - 1;
            }
        }
    }

    // =========================================================
    // SEARCH USING BINARY SEARCH
    // =========================================================

    public void search(
            String pattern) {

        if (
                pattern == null
                ||
                pattern.isEmpty()
        ) {

            System.out.println(
                    "Pattern cannot be empty."
            );

            return;
        }

        int first =
                lowerBound(pattern);

        if (
                first >=
                suffixArray.length
        ) {

            System.out.println(
                    "Pattern Not Found"
            );

            return;
        }

        if (
                !startsWith(
                        suffixArray[first],
                        pattern
                )
        ) {

            System.out.println(
                    "Pattern Not Found"
            );

            return;
        }

        boolean found = false;

        for (
                int i = first;
                i < suffixArray.length;
                i++
        ) {

            int position =
                    suffixArray[i];

            if (
                    startsWith(
                            position,
                            pattern
                    )
            ) {

                System.out.println(
                        "Pattern Found at Index : "
                        + position
                );

                found = true;

            } else {

                break;
            }
        }

        if (!found) {

            System.out.println(
                    "Pattern Not Found"
            );
        }
    }

    // =========================================================
    // LOWER BOUND
    // =========================================================

    private int lowerBound(
            String pattern) {

        int left = 0;

        int right =
                suffixArray.length;

        while (left < right) {

            int mid =
                    left
                    +
                    (right - left) / 2;

            int comparison =
                    compareSuffix(
                            suffixArray[mid],
                            pattern
                    );

            if (comparison < 0) {

                left =
                        mid + 1;

            } else {

                right = mid;
            }
        }

        return left;
    }

    // =========================================================
    // COMPARE SUFFIX WITH PATTERN
    // =========================================================

    private int compareSuffix(
            int suffixIndex,
            String pattern) {

        int i =
                suffixIndex;

        int j = 0;

        while (
                i < text.length()
                &&
                j < pattern.length()
        ) {

            char a =
                    text.charAt(i);

            char b =
                    pattern.charAt(j);

            if (a < b) {
                return -1;
            }

            if (a > b) {
                return 1;
            }

            i++;
            j++;
        }

        /*
         * Pattern is completely matched.
         */

        if (
                j ==
                pattern.length()
        ) {

            return 0;
        }

        /*
         * Suffix ended before pattern.
         */

        return -1;
    }

    // =========================================================
    // CHECK PREFIX
    // =========================================================

    private boolean startsWith(
            int suffixIndex,
            String pattern) {

        if (
                suffixIndex < 0
                ||
                suffixIndex +
                        pattern.length()
                        >
                        text.length()
        ) {

            return false;
        }

        for (
                int i = 0;
                i < pattern.length();
                i++
        ) {

            if (
                    text.charAt(
                            suffixIndex + i
                    )
                    !=
                    pattern.charAt(i)
            ) {

                return false;
            }
        }

        return true;
    }

    // =========================================================
    // GET SUFFIX ARRAY
    // =========================================================

    public int[] getSuffixArray() {

        return Arrays.copyOf(
                suffixArray,
                suffixArray.length
        );
    }

    // =========================================================
    // DISPLAY SUFFIX ARRAY
    // =========================================================

    public void displaySuffixArray(
            int limit) {

        System.out.println(
                "\n========== SA-IS SUFFIX ARRAY =========="
        );

        int count =
                Math.min(
                        limit,
                        suffixArray.length
                );

        for (
                int i = 0;
                i < count;
                i++
        ) {

            int position =
                    suffixArray[i];

            String suffix =
                    text.substring(
                            position,
                            Math.min(
                                    position + 50,
                                    text.length()
                            )
                    ).replace(
                            "\n",
                            " "
                    );

            System.out.println(
                    i
                    + " -> Index "
                    + position
                    + " : "
                    + suffix
            );
        }

        if (
                suffixArray.length >
                limit
        ) {

            System.out.println(
                    "... "
                    +
                    (
                            suffixArray.length
                            - limit
                    )
                    +
                    " more suffixes"
            );
        }
    }
}