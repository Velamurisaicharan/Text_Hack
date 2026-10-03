package pps;

public class KMP {

    // Create LPS array
    static void lpsArray(String pat, int lps[]) {
        int len = 0;
        lps[0] = 0;

        int i = 1;
        while (i < pat.length()) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0)
                    len = lps[len - 1];
                else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
    }

    // KMP Search
    static void search(String text, String pat) {
        int n = text.length();
        int m = pat.length();

        int lps[] = new int[m];
        lpsArray(pat, lps);

        int i = 0, j = 0;

        while (i < n) {
            if (text.charAt(i) == pat.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                System.out.println("Pattern found at index " + (i - j));
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pat.charAt(j)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i++;
            }
        }
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";

        search(text, pattern);
    }
}