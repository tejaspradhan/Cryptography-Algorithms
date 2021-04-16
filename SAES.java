import java.util.*;

public class SAES {
    static int[] plainText;
    static int[] key;
    static int[] cipherText;
    static int[][] w;
    static int[] k1, k2, k3;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        key = new int[16];
        k1 = new int[16];
        k2 = new int[16];
        k3 = new int[16];
        w = new int[6][8];
        int c;

        System.out.println("Enter 16 bit key");
        for (int i = 0; i < key.length; i++) {
            key[i] = sc.nextInt();
        }
        generateKey(key);
        System.out.println("1.Encrpytion\n2. Decryption");
        c = sc.nextInt();

        if (c == 1) {
            plainText = new int[16];
            System.out.println("Enter 16 bit plain text");
            for (int i = 0; i < plainText.length; i++) {
                plainText[i] = sc.nextInt();
            }

            // add round key 1
            int[] ark1 = XOR(plainText, k1);

            // substitute nibble
            int[] ark1w1 = new int[8];
            int[] ark1w2 = new int[8];
            for (int i = 0; i < 8; i++) {
                ark1w1[i] = ark1[i];
                ark1w2[i] = ark1[i + 8];
            }
            ark1w1 = subNibble(ark1w1, false);
            ark1w2 = subNibble(ark1w2, false);
            for (int i = 0; i < 8; i++) {
                ark1[i] = ark1w1[i];
                ark1[i + 8] = ark1w2[i];
            }

            // shift rows
            ark1 = shiftRows(ark1);
            int[][] sMat = { { 1, 4 }, { 4, 1 } };
            int[] ark2 = mixColumns(ark1, sMat);

            // add round key 2
            ark2 = XOR(ark2, k2);

            // substitute nibble
            int[] ark2w1 = new int[8];
            int[] ark2w2 = new int[8];
            for (int i = 0; i < 8; i++) {
                ark2w1[i] = ark2[i];
                ark2w2[i] = ark2[i + 8];
            }
            ark2w1 = subNibble(ark2w1, false);
            ark2w2 = subNibble(ark2w2, false);
            int[] ark3 = new int[ark2.length];
            for (int i = 0; i < 8; i++) {
                ark3[i] = ark2w1[i];
                ark3[i + 8] = ark2w2[i];
            }
            ark3 = XOR(ark3, k3);
            System.out.print("\nCipher Text: ");
            for (int i = 0; i < ark3.length; i++) {
                System.out.print(ark3[i] + "");
                if ((i + 1) % 4 == 0)
                    System.out.print(" ");
            }
            System.out.println();
        }

        else if (c == 2) { // decryption
            cipherText = new int[16];
            System.out.println("Enter 16 bit cipher text");
            for (int i = 0; i < cipherText.length; i++) {
                cipherText[i] = sc.nextInt();
            }
            int[] ark3 = XOR(cipherText, k3);
            int[] ark2 = new int[ark3.length];
            // inverse shift row
            ark3 = shiftRows(ark3);

            // inverse substitute nibble
            int[] ark3w1 = new int[8];
            int[] ark3w2 = new int[8];
            for (int i = 0; i < 8; i++) {
                ark3w1[i] = ark3[i];
                ark3w2[i] = ark3[i + 8];
            }
            ark3w1 = subNibble(ark3w1, true);
            ark3w2 = subNibble(ark3w2, true);
            for (int i = 0; i < 8; i++) {
                ark2[i] = ark3w1[i];
                ark2[i + 8] = ark3w2[i];
            }

            // add round key 2
            ark2 = XOR(ark2, k2);
            // inverse mix column
            int[][] invSMat = { { 9, 2 }, { 2, 9 } };
            int[] ark1 = mixColumns(ark2, invSMat);
            // inverse shift row
            ark1 = shiftRows(ark1);

            // inverse nibble substitution
            int[] ark1w1 = new int[8];
            int[] ark1w2 = new int[8];
            for (int i = 0; i < 8; i++) {
                ark1w1[i] = ark1[i];
                ark1w2[i] = ark1[i + 8];
            }
            ark1w1 = subNibble(ark1w1, true);
            ark1w2 = subNibble(ark1w2, true);
            for (int i = 0; i < 8; i++) {
                ark1[i] = ark1w1[i];
                ark1[i + 8] = ark1w2[i];
            }

            // add round key 1
            ark1 = XOR(ark1, k1);
            System.out.println("Decrypted Text : ");
            for (int i = 0; i < ark1.length; i++) {
                System.out.print(ark1[i] + " ");
                if ((i + 1) % 4 == 0)
                    System.out.print(" ");
            }
            System.out.println();
        }

        else {
            System.out.println("Enter valid choice");
        }
        sc.close();
    } // end of main

    static int[] shiftRows(int[] x) {
        int[] result = new int[x.length];
        for (int i = 0; i < 4; i++) {
            result[i] = x[i];
            result[i + 4] = x[i + 12];
            result[i + 8] = x[i + 8];
            result[i + 12] = x[i + 4];
        }
        return result;
    }

    static int[] XOR(int[] a, int[] b) {
        int[] result = new int[a.length];
        if (a.length != b.length)
            System.out.println("Length not matching ");
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b[i])
                result[i] = 0;
            else
                result[i] = 1;
        }
        return result;
    }

    static int[] rotNibble(int[] x) {
        int[] result = new int[x.length];
        for (int i = 0; i < 4; i++) {
            result[i] = x[i + 4];
            result[i + 4] = x[i];
        }
        return result;
    }

    static int[] subNibble(int[] x, boolean inverse) {
        String n1 = "", n2 = "";
        int[] result = new int[x.length];
        HashMap<String, String> sBox = new HashMap<>();
        HashMap<String, String> invSBox = new HashMap<>();
        sBox.put("0000", "1001");
        sBox.put("0001", "0100");
        sBox.put("0010", "1010");
        sBox.put("0011", "1011");
        sBox.put("0100", "1101");
        sBox.put("0101", "0001");
        sBox.put("0110", "1000");
        sBox.put("0111", "0101");
        sBox.put("1000", "0110");
        sBox.put("1001", "0010");
        sBox.put("1010", "0000");
        sBox.put("1011", "0011");
        sBox.put("1100", "1100");
        sBox.put("1101", "1110");
        sBox.put("1110", "1111");
        sBox.put("1111", "0111");

        invSBox.put("1001", "0000");
        invSBox.put("0100", "0001");
        invSBox.put("1010", "0010");
        invSBox.put("1011", "0011");
        invSBox.put("1101", "0100");
        invSBox.put("0001", "0101");
        invSBox.put("1000", "0110");
        invSBox.put("0101", "0111");
        invSBox.put("0110", "1000");
        invSBox.put("0010", "1001");
        invSBox.put("0000", "1010");
        invSBox.put("0011", "1011");
        invSBox.put("1100", "1100");
        invSBox.put("1110", "1101");
        invSBox.put("1111", "1110");
        invSBox.put("0111", "1111");

        if (inverse) { // inverse substitution
            for (int i = 0; i < 4; i++) {
                n1 += Integer.toString(x[i]);
                n2 += Integer.toString(x[i + 4]);
            }
            n1 = invSBox.get(n1);
            n2 = invSBox.get(n2);
            for (int i = 0; i < 4; i++) {
                result[i] = ((int) n1.charAt(i)) - 48;
                result[i + 4] = ((int) n2.charAt(i)) - 48;
            }
        }

        else {
            for (int i = 0; i < 4; i++) {
                n1 += Integer.toString(x[i]);
                n2 += Integer.toString(x[i + 4]);
            }
            n1 = sBox.get(n1);
            n2 = sBox.get(n2);
            for (int i = 0; i < 4; i++) {
                result[i] = ((int) n1.charAt(i)) - 48;
                result[i + 4] = ((int) n2.charAt(i)) - 48;
            }
        }

        return result;
    }

    static void generateKey(int[] k) {
        k1 = k;
        for (int i = 0; i < 8; i++) {
            w[0][i] = k[i];
            w[1][i] = k[i + 8];
        }
        int count = 0;
        for (int i = 2; i < 6; i++) {
            if (i % 2 == 0) {

                w[i] = XOR(w[i - 2], subNibble(rotNibble(w[i - 1]), false));
                while (count < 2) {
                    if (count == 0) {
                        int[] temp = { 1, 0, 0, 0, 0, 0, 0, 0 };
                        w[i] = XOR(w[i], temp);
                        count++;
                        break;
                    }

                    else {
                        System.out.println();
                        int[] temp = { 0, 0, 1, 1, 0, 0, 0, 0 };
                        w[i] = XOR(w[i], temp);
                        count++;
                        break;
                    }
                }
            }

            else {
                w[i] = XOR(w[i - 2], w[i - 1]);
            }
        }
        for (int i = 0; i < 8; i++) {
            k2[i] = w[2][i];
            k2[i + 8] = w[3][i];
            k3[i] = w[4][i];
            k3[i + 8] = w[5][i];
        }

    }

    static int[] mixColumns(int[] x, int[][] sMat) {
        int[] result = new int[x.length];
        String s = "";
        for (int i = 0; i < x.length; i++) {
            s += Integer.toString(x[i]);
        }

        int[] s00 = XOR(reduce(multiply(dec2bin(sMat[0][0]), dec2bin(bin2dec(s.substring(0, 4))))),
                reduce(multiply(dec2bin(sMat[0][1]), dec2bin(bin2dec(s.substring(4, 8))))));
        int[] s01 = XOR(reduce(multiply(dec2bin(sMat[1][0]), dec2bin(bin2dec(s.substring(0, 4))))),
                reduce(multiply(dec2bin(sMat[1][1]), dec2bin(bin2dec(s.substring(4, 8))))));

        int[] s10 = XOR(reduce(multiply(dec2bin(sMat[0][0]), dec2bin(bin2dec(s.substring(8, 12))))),
                reduce(multiply(dec2bin(sMat[0][1]), dec2bin(bin2dec(s.substring(12, 16))))));

        int[] s11 = XOR(reduce(multiply(dec2bin(sMat[1][0]), dec2bin(bin2dec(s.substring(8, 12))))),
                reduce(multiply(dec2bin(sMat[1][1]), dec2bin(bin2dec(s.substring(12, 16))))));

        for (int i = 0; i < 4; i++) {
            result[i] = s00[i];
            result[i + 4] = s01[i];
            result[i + 8] = s10[i];
            result[i + 12] = s11[i];
        }
        return result;
    }

    static int[] multiply(int[] x, int[] y) {

        int maxLen = x.length + y.length - 1;
        int[] result = new int[maxLen];
        for (int i = 0; i < maxLen; i++) {
            result[i] = 0;
        }

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y.length; j++) {
                result[i + j] += x[i] * y[j];
            }
        }
        Queue<Integer> q = new LinkedList<>();
        int count = 0;
        while (result[count] == 0) {
            count++;
            continue;
        }
        for (int i = count; i < result.length; i++) {
            q.add(result[i]);
        }
        int i = 0;
        if (q.size() > 4) {
            result = new int[q.size()];
            while (!q.isEmpty())
                result[i++] = q.poll();
        }

        else {
            result = new int[4];
            while (i < 4 - q.size()) {
                result[i++] = 0;
            }
            while (!q.isEmpty()) {
                result[i++] = q.poll();
            }
        }
        return result;
    }

    static int bin2dec(String x) {
        int decimal = 0;
        int factor = 0;
        for (int i = x.length() - 1; i >= 0; i--) {
            if (x.charAt(i) == '1')
                decimal += Math.pow(2, factor);
            factor++;
        }
        return decimal;
    }

    static int[] dec2bin(int x) {
        if (x == 0) {
            int[] result = { 0, 0 };
            return result;
        }
        String s = "";
        while (x > 0) {
            s += Integer.toString(x % 2);
            x /= 2;
        }
        while (s.length() < 4) {
            s += "0";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.reverse();
        String r = sb.toString();
        int[] result = new int[r.length()];
        for (int i = 0; i < r.length(); i++) {
            result[i] = r.charAt(i) - 48;
        }
        return result;
    }

    static int[] reduce(int[] x) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] % 2 == 0)
                x[i] = 0;
            else
                x[i] = 1;
        }
        if (x.length == 4)
            return x;
        int[] result = new int[4];
        int[] reducer = { 1, 0, 0, 1, 1 };
        int[] temp = new int[5];

        Queue<Integer> q = new LinkedList<>();
        int rem = x.length;
        for (int i = 0; i < x.length; i++)
            q.add(x[i]);
        while (q.size() > 4) {
            for (int i = 0; i < 5; i++) {
                temp[i] = q.poll();
                rem--;
            }
            temp = XOR(temp, reducer);
            q = new LinkedList<>();
            for (int i = 0; i < temp.length; i++) {
                q.add(temp[i]);
            }
            while (rem > 0) {
                q.add(x[x.length - rem]);
                rem--;
            }
            while (q.peek() == 0)
                q.poll();
        }
        if (q.size() == 4) {
            for (int i = 0; i < 4; i++) {
                result[i] = q.poll();
            }
        }

        else {
            int i = 0;
            while (i < 4 - q.size())
                result[i++] = 0;
            while (!q.isEmpty())
                result[i++] = q.poll();
        }
        return result;
    }
}
