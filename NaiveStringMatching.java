package pps;


public class NaiveStringMatching {

    public static void search(String pattern, String text) {

        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {

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
    }
}