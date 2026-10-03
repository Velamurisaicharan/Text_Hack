package pps;

import java.util.Arrays;

public class SuffixArray {

    private int[] suffixArray;
    private String text;

    // Constructor
    public SuffixArray(String text) {
        this.text = text;
        buildSuffixArray();
    }

    // =========================================================
    // BUILD SUFFIX ARRAY
    // =========================================================
    private void buildSuffixArray() {

        int n = text.length();

        suffixArray = new int[n];

        if (n == 0) {
            return;
        }

        int[] rank = new int[n];
        int[] newRank = new int[n];
        int[] temp = new int[n];

        // Initial suffix indexes
        for (int i = 0; i < n; i++) {
            suffixArray[i] = i;
            rank[i] = text.charAt(i);
        }

        // Sort suffixes according to first character
        countingSortByCharacter(suffixArray);

        // Assign initial ranks
        int classes = 1;

        newRank[suffixArray[0]] = 0;

        for (int i = 1; i < n; i++) {

            if (text.charAt(suffixArray[i])
                    != text.charAt(suffixArray[i - 1])) {
                classes++;
            }

            newRank[suffixArray[i]] = classes - 1;
        }

        int[] swap = rank;
        rank = newRank;
        newRank = swap;

        // Doubling technique
        for (int k = 1; k < n; k *= 2) {

            // Sort by second part
            countingSort(suffixArray, temp, rank, k);

            // Sort by first part
            countingSort(temp, suffixArray, rank, 0);

            // Recalculate ranks
            newRank[suffixArray[0]] = 0;

            int newClasses = 1;

            for (int i = 1; i < n; i++) {

                int current = suffixArray[i];
                int previous = suffixArray[i - 1];

                if (rank[current] != rank[previous]
                        || getRank(rank, current + k)
                        != getRank(rank, previous + k)) {

                    newClasses++;
                }

                newRank[current] = newClasses - 1;
            }

            swap = rank;
            rank = newRank;
            newRank = swap;

            classes = newClasses;

            if (classes == n) {
                break;
            }

            // Avoid integer overflow
            if (k > n / 2) {
                break;
            }
        }
    }

    // =========================================================
    // INITIAL CHARACTER SORT
    // =========================================================
    private void countingSortByCharacter(int[] array) {

        int[] count = new int[Character.MAX_VALUE + 2];

        for (int index : array) {
            count[text.charAt(index) + 1]++;
        }

        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        int[] result = new int[array.length];

        for (int index : array) {
            int key = text.charAt(index) + 1;
            result[count[key - 1]++] = index;
        }

        System.arraycopy(result, 0, array, 0, array.length);
    }

    // =========================================================
    // COUNTING SORT FOR DOUBLING
    // =========================================================
    private void countingSort(
            int[] input,
            int[] output,
            int[] rank,
            int offset) {

        int n = input.length;

        int maxRank = 0;

        for (int value : rank) {
            maxRank = Math.max(maxRank, value);
        }

        int[] count = new int[maxRank + 2];

        // Count
        for (int index : input) {

            int key = getRank(rank, index + offset) + 1;

            count[key]++;
        }

        // Prefix sum
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Stable sort
        for (int i = n - 1; i >= 0; i--) {

            int index = input[i];

            int key = getRank(rank, index + offset) + 1;

            output[--count[key]] = index;
        }
    }

    // Get rank of suffix position
    private int getRank(int[] rank, int index) {

        if (index >= rank.length) {
            return -1;
        }

        return rank[index];
    }

    // =========================================================
    // SEARCH USING BINARY SEARCH
    // =========================================================
    public void search(String pattern) {

        if (pattern == null || pattern.isEmpty()) {
            System.out.println("Pattern cannot be empty.");
            return;
        }

        if (text.length() == 0) {
            System.out.println("Pattern Not Found");
            return;
        }

        int first = lowerBound(pattern);

        if (first == suffixArray.length
                || !startsWith(suffixArray[first], pattern)) {

            System.out.println("Pattern Not Found");
            return;
        }

        boolean found = false;

        for (int i = first; i < suffixArray.length; i++) {

            int position = suffixArray[i];

            if (startsWith(position, pattern)) {

                System.out.println(
                        "Pattern Found at Index : " + position
                );

                found = true;

            } else {
                break;
            }
        }

        if (!found) {
            System.out.println("Pattern Not Found");
        }
    }

    // =========================================================
    // FIND FIRST POSSIBLE MATCH
    // =========================================================
    private int lowerBound(String pattern) {

        int left = 0;
        int right = suffixArray.length;

        while (left < right) {

            int mid = left + (right - left) / 2;

            int comparison = compareSuffixWithPattern(
                    suffixArray[mid],
                    pattern
            );

            if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    // =========================================================
    // COMPARE SUFFIX WITH PATTERN
    // =========================================================
    private int compareSuffixWithPattern(
            int suffixIndex,
            String pattern) {

        int i = suffixIndex;
        int j = 0;

        while (i < text.length()
                && j < pattern.length()) {

            char c1 = text.charAt(i);
            char c2 = pattern.charAt(j);

            if (c1 != c2) {
                return Character.compare(c1, c2);
            }

            i++;
            j++;
        }

        // Pattern is prefix of suffix
        if (j == pattern.length()) {
            return 0;
        }

        // Suffix ended before pattern
        return -1;
    }

    // =========================================================
    // CHECK WHETHER SUFFIX STARTS WITH PATTERN
    // =========================================================
    private boolean startsWith(
            int suffixIndex,
            String pattern) {

        if (suffixIndex + pattern.length() > text.length()) {
            return false;
        }

        for (int i = 0; i < pattern.length(); i++) {

            if (text.charAt(suffixIndex + i)
                    != pattern.charAt(i)) {

                return false;
            }
        }

        return true;
    }

    // =========================================================
    // DISPLAY SUFFIX ARRAY
    // =========================================================
    public void displaySuffixArray(int limit) {

        System.out.println("\n========== SUFFIX ARRAY ==========");

        int count = Math.min(limit, suffixArray.length);

        for (int i = 0; i < count; i++) {

            int index = suffixArray[i];

            System.out.println(
                    i + " -> Index " + index
                    + " : "
                    + text.substring(
                            index,
                            Math.min(index + 50, text.length())
                    ).replace("\n", " ")
            );
        }

        if (suffixArray.length > limit) {
            System.out.println(
                    "... " + (suffixArray.length - limit)
                    + " more suffixes"
            );
        }
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
}