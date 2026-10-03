package pps;


public class RabinKarp {

    public static void search(String pattern, String text, int q) {

        int n = text.length();
        int m = pattern.length();

        int patternHash = 0;
        int textHash = 0;

        // Pattern Hash
        for (int i = 0; i < m; i++) {
            patternHash += pattern.charAt(i);
        }

        // First Window Hash
        for (int i = 0; i < m; i++) {
            textHash += text.charAt(i);
        }

        // Slide Pattern
        for (int i = 0; i <= n - m; i++) {

            if (patternHash == textHash) {

                int j;

                for (j = 0; j < m; j++) {

                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }
                }

                if (j == m) {
                    System.out.println("Pattern found at index : " + i);
                }
            }

            if (i < n - m) {

                textHash = textHash - text.charAt(i);

                textHash = textHash + text.charAt(i + m);
            }
        }
    }
}