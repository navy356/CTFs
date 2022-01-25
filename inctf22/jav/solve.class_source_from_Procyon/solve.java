import java.util.Scanner;

// 
// Decompiled by Procyon v0.5.36
// 

class solve
{
    private static int length;
    
    public static void main(final String[] array) {
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the flag: ");
        final String next = scanner.next();
        final String substring = next.substring(0, solve.length);
        final String substring2 = next.substring(solve.length, next.length() - 1);
        final String substring3 = next.substring(next.length() - 1);
        if (substring.equals("inctf{") && substring3.equals("}")) {
            if (substring2.length() == solve.length * solve.length) {
                if (func(substring2)) {
                    System.out.println("Correct");
                }
                else {
                    System.out.println("Incorrect");
                }
            }
            else {
                System.out.println("Incorrect length");
            }
        }
        else {
            System.out.println("Incorrect format");
        }
    }
    
    public static String lrotate(final char[] value, final int n) {
        for (int i = 0; i < n; ++i) {
            final char c = value[0];
            int j;
            for (j = 0; j < solve.length * 2 - 1; ++j) {
                value[j] = value[j + 1];
            }
            value[j] = c;
        }
        return new String(value);
    }
    
    public static char[] get_array(final char[][] array, final int n, final int n2) {
        final char[] array2 = new char[solve.length * 2];
        int n3 = 0;
        for (int i = n; i <= n2; ++i) {
            for (int j = 0; j < solve.length; ++j) {
                array2[n3] = array[i][j];
                ++n3;
            }
        }
        return array2;
    }
    
    public static char[] Shuffle(final char[] array) {
        int n = 0;
        int n2 = solve.length * 2 - 1;
        final char[] array2 = new char[solve.length * 2];
        for (int i = 0; i < solve.length * 2; i += 2) {
            array2[i] = array[n2];
            array2[i + 1] = array[n];
            --n2;
            ++n;
        }
        return array2;
    }
    
    public static char[] Shuffle(final String s, final int n) {
        final char[] charArray = s.toCharArray();
        int n2 = n / 2;
        int n3 = n2 - 1;
        final char[] array = new char[n];
        final char[] array2 = new char[n];
        for (int i = 0; i < n; i += 2) {
            array[i] = charArray[n3];
            array[i + 1] = charArray[n2];
            --n3;
            ++n2;
        }
        for (int j = 0; j < n; ++j) {
            array2[j] = array[n - (j ^ 0x1) - 1];
        }
        return array2;
    }
    
    public static boolean func(final String s) {
        int n = 0;
        final char[][] array = new char[solve.length][solve.length];
        final char[] array2 = new char[36];
        for (int i = 0; i < solve.length; ++i) {
            for (int j = 0; j < solve.length; ++j) {
                if (i % 2 == 0) {
                    array[j][i] = s.charAt(n);
                }
                else {
                    array[solve.length - j - 1][i] = s.charAt(n);
                }
                ++n;
            }
        }
        return "-t3drtDm3_c42@03Vxx-3Tr1_173351rwR-4".equals(new String(Shuffle(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, lrotate(Shuffle(get_array(array, 0, 1)), 7), lrotate(Shuffle(get_array(array, 2, 3)), 8), lrotate(Shuffle(get_array(array, 4, 5)), 10)), solve.length * 6)));
    }
    
    static {
        solve.length = 6;
    }
}
