package pps;

public class ZAlgorithm {

    public static void search(String pattern, String text) {

        String concat = pattern + "$" + text;

        int n = concat.length();

        int[] Z = new int[n];

        int L = 0, R = 0;

        // Construct Z Array
        for (int i = 1; i < n; i++) {

            if (i > R) {

                L = R = i;

                while (R < n && concat.charAt(R - L) == concat.charAt(R)) {
                    R++;
                }

                Z[i] = R - L;
                R--;

            } else {

                int k = i - L;

                if (Z[k] < R - i + 1) {

                    Z[i] = Z[k];

                } else {

                    L = i;

                    while (R < n && concat.charAt(R - L) == concat.charAt(R)) {
                        R++;
                    }

                    Z[i] = R - L;
                    R--;
                }
            }
        }

        boolean found = false;

        for (int i = 0; i < n; i++) {

            if (Z[i] == pattern.length()) {

                System.out.println("Pattern found at index : " + (i - pattern.length() - 1));
                found = true;
            }
        }

        if (!found) {
            System.out.println("Pattern Not Found");
        }
    }
}