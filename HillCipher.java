import java.util.*;

public class HillCipher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int c;
        System.out.println("1. Encryption\n2. Decryption");
        c = sc.nextInt();
        if (c == 1)
            encrypt();
        else
            decrypt();
        sc.close();
    }

    static void encrypt() {
        Scanner sc = new Scanner(System.in);
        int[][] keyMatrix = new int[2][2];
        int[] text = new int[2];
        int[] conv = new int[2];
        String plainText;
        System.out.println("Enter plain text : ");
        plainText = sc.next();
        System.out.println("Enter key matrix : ");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++)
                keyMatrix[i][j] = sc.nextInt();
        }
        String cipherText = "";

        for (int i = 0; i < plainText.length() - 1; i += 2) {
            if (Character.isUpperCase(plainText.charAt(i)))
                text[0] = plainText.charAt(i) - 'A';
            else
                text[0] = plainText.charAt(i) - 'a';

            if (Character.isUpperCase(plainText.charAt(i + 1)))
                text[1] = plainText.charAt(i + 1) - 'A';
            else
                text[1] = plainText.charAt(i + 1) - 'a';

            conv[0] = (text[0] * keyMatrix[0][0] + text[1] * keyMatrix[0][1]) % 26;
            conv[1] = (text[0] * keyMatrix[1][0] + text[1] * keyMatrix[1][1]) % 26;

            if (Character.isUpperCase(plainText.charAt(i)))
                conv[0] += 'A';
            else
                conv[0] += 'a';

            if (Character.isUpperCase(plainText.charAt(i + 1)))
                conv[1] += 'A';
            else
                conv[1] += 'a';

            cipherText += ((char) conv[0]);
            cipherText += ((char) conv[1]);
        }
        System.out.println("Cipher Text : " + cipherText);
        sc.close();

    }

    static void decrypt() {
        Scanner sc = new Scanner(System.in);
        int[][] keyMatrix = new int[2][2];
        int[] text = new int[2];
        int[] conv = new int[2];
        String cipherText;
        System.out.println("Enter cipher text : ");
        cipherText = sc.next();
        System.out.println("Enter key matrix : ");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++)
                keyMatrix[i][j] = sc.nextInt();
        }
        keyMatrix = calcMatInverse(keyMatrix);
        String plainText = "";

        for (int i = 0; i < cipherText.length() - 1; i += 2) {
            if (Character.isUpperCase(cipherText.charAt(i)))
                text[0] = cipherText.charAt(i) - 'A';
            else
                text[0] = cipherText.charAt(i) - 'a';

            if (Character.isUpperCase(cipherText.charAt(i + 1)))
                text[1] = cipherText.charAt(i + 1) - 'A';
            else
                text[1] = cipherText.charAt(i + 1) - 'a';

            conv[0] = (text[0] * keyMatrix[0][0] + text[1] * keyMatrix[0][1]) % 26;
            conv[1] = (text[0] * keyMatrix[1][0] + text[1] * keyMatrix[1][1]) % 26;

            if (Character.isUpperCase(cipherText.charAt(i)))
                conv[0] += 'A';
            else
                conv[0] += 'a';

            if (Character.isUpperCase(cipherText.charAt(i + 1)))
                conv[1] += 'A';
            else
                conv[1] += 'a';
            plainText += ((char) conv[0]);
            plainText += ((char) conv[1]);
        }
        System.out.println("Decrypted Text : " + plainText);
        sc.close();
    }

    static int[][] calcMatInverse(int[][] mat) {
        int det = (mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]);
        int invDet = findInv(det);
        if (invDet == -1) {
            System.out.println("Please enter a valid key matrix");
            System.exit(-1);
        }
        int[][] inv = new int[2][2];
        inv[0][0] = mat[1][1];
        inv[0][1] = -1 * mat[0][1];
        inv[1][0] = -1 * mat[1][0];
        inv[1][1] = mat[0][0];
        if (inv[0][1] < 0)
            inv[0][1] += 26;
        if (inv[1][0] < 0)
            inv[1][0] += 26;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                inv[i][j] = (inv[i][j] * invDet) % 26;
                if (inv[i][j] < 0)
                    inv[i][j] += 26;
            }
        }
        System.out.println();
        return inv;
    }

    static int makePositive(int a) {
        while (a < 0)
            a += 26;
        return a;
    }

    static int findInv(int x) {
        x %= 26;
        for (int i = 0; i < x; i++) {
            if ((x * i) % 26 == 1)
                return i;
        }

        for (int i = 0; i > (-1 * x); i--) {
            if (makePositive((x * i)) % 26 == 1)
                return i;
        }

        return -1;
    }
}
