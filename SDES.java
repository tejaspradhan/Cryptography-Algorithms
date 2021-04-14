import java.util.*;

public class SDES {
    static int[][] S0 = { { 1, 0, 3, 2 }, { 3, 2, 1, 0 }, { 0, 2, 1, 3 }, { 3, 1, 3, 2 } };
    static int[][] S1 = { { 0, 1, 2, 3 }, { 2, 0, 1, 3 }, { 3, 0, 1, 0 }, { 2, 1, 0, 3 } };
    static int[] P10 = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
    static int[] P8 = { 6, 3, 7, 4, 8, 5, 10, 9 };
    static int[] P4 = { 2, 4, 3, 1 };
    static int[] IP = { 2, 6, 3, 1, 4, 8, 5, 7 };
    static int[] IPINV = { 4, 1, 3, 5, 7, 2, 8, 6 };
    static int[] EP = { 4, 1, 2, 3, 2, 3, 4, 1 };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int c;
        int[] key = new int[10];
        int[] k1 = new int[8];
        int[] k2 = new int[8];

        System.out.println("Enter 10-bit key : ");
        for (int i = 0; i < key.length; i++) {
            key[i] = sc.nextInt();
        }

        // applying p10
        int[] temp = new int[10];
        for (int i = 0; i < key.length; i++) {
            temp[i] = key[P10[i] - 1];
        }
        key = temp;
        // LS-1
        key = leftShift(key);
        // applying P8
        for (int i = 0; i < k1.length; i++) {
            k1[i] = key[P8[i] - 1];
        }
        // LS-2
        key = leftShift(leftShift(key));

        // applying P8
        for (int i = 0; i < k2.length; i++) {
            k2[i] = key[P8[i] - 1];
        }

        System.out.println("1. Encrypt\n2. Decrypt");
        c = sc.nextInt();
        if (c == 1)
            encrypt(k1, k2);
        else
            decrypt(k1, k2);
        sc.close();

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

    static int[] leftShift(int[] key) {
        int t1 = key[0];
        int t2 = key[5];
        int i;
        for (i = 0; i < 4; i++) {
            key[i] = key[i + 1];
            key[i + 5] = key[i + 6];
        }
        key[i] = t1;
        key[i + 5] = t2;
        return key;
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

    static String dec2bin(int x) {
        if (x == 0)
            return "00";
        String s = "";
        while (x > 0) {
            s += Integer.toString(x % 2);
            x /= 2;
        }
        if (s.length() == 1)
            s += 0;
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.reverse();
        return sb.toString();
    }

    static int[] initialPermutation(int[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++)
            result[i] = data[IP[i] - 1];
        return result;
    }

    static int[] inversePermutation(int[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++)
            result[i] = data[IPINV[i] - 1];
        return result;
    }

    static int[] substitute(int[] temp) {
        int[] res = new int[4];
        String r1 = "", c1 = "", r2 = "", c2 = "";
        int val1, val2;
        String s = "";
        r1 += Integer.toString(temp[0]);
        r1 += Integer.toString(temp[3]);
        c1 += Integer.toString(temp[1]);
        c1 += Integer.toString(temp[2]);
        r2 += Integer.toString(temp[4]);
        r2 += Integer.toString(temp[7]);
        c2 += Integer.toString(temp[5]);
        c2 += Integer.toString(temp[6]);

        val1 = S0[bin2dec(r1)][bin2dec(c1)];
        val2 = S1[bin2dec(r2)][bin2dec(c2)];

        s += dec2bin(val1) + dec2bin(val2);
        for (int i = 0; i < s.length(); i++) {
            res[i] = (int) s.charAt(i) - 48;
        }
        int[] tempRes = new int[res.length];
        for (int i = 0; i < res.length; i++) {
            tempRes[i] = res[P4[i] - 1];
        }
        res = tempRes;
        return res;
    }

    static int[] roundFunction(int[] left, int[] right, int[] k) {
        int[] AEP = new int[8];
        int[] temp = new int[8];
        int[] result = new int[4];
        for (int i = 0; i < AEP.length; i++) {
            AEP[i] = right[EP[i] - 1];
        }
        temp = XOR(AEP, k);
        result = substitute(temp);
        result = XOR(left, result);
        return result;
    }

    static void encrypt(int[] k1, int[] k2) {
        Scanner sc = new Scanner(System.in);
        int[] data = new int[8];
        int[] left = new int[4];
        int[] right = new int[4];
        int[] result = new int[8];
        int[] tempLeft = new int[4];

        System.out.println("Enter data : ");
        for (int i = 0; i < data.length; i++) {
            data[i] = sc.nextInt();
        }
        // initial permutation
        data = initialPermutation(data);
        // splitting into left and right halves
        for (int i = 0; i < 4; i++) {
            left[i] = data[i];
            right[i] = data[i + 4];
        }
        // // applying round function for key 1
        left = roundFunction(left, right, k1);

        // swapping nibbles
        tempLeft = left;
        left = right;
        right = tempLeft;

        // applying round function for key2
        left = roundFunction(left, right, k2);
        for (int i = 0; i < 4; i++) {
            result[i] = left[i];
            result[i + 4] = right[i];
        }
        result = inversePermutation(result);
        System.out.println("Encrypted Data : ");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
        sc.close();
    }

    static void decrypt(int[] k1, int[] k2) {
        Scanner sc = new Scanner(System.in);
        int[] cipher = new int[8];
        int[] left = new int[4];
        int[] right = new int[4];
        int[] result = new int[8];
        int[] tempLeft = new int[4];

        System.out.println("Enter cipher  : ");
        for (int i = 0; i < cipher.length; i++) {
            cipher[i] = sc.nextInt();
        }

        // initial permutation for the cipher
        cipher = initialPermutation(cipher);
        for (int i = 0; i < 4; i++) {
            left[i] = cipher[i];
            right[i] = cipher[i + 4];
        }
        // applying round function for key 2
        left = roundFunction(left, right, k2);

        // swapping nibbles
        tempLeft = left;
        left = right;
        right = tempLeft;

        left = roundFunction(left, right, k1);
        for (int i = 0; i < 4; i++) {
            result[i] = left[i];
            result[i + 4] = right[i];
        }
        result = inversePermutation(result);
        System.out.println("Decrypted Data : ");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
        sc.close();
    }

}

// Test Case
// 1 0 1 0 0 1 0 1
// 0 0 1 0 0 1 0 1 1 1
