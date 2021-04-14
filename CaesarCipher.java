import java.util.*;

public class CaesarCipher {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println(
                    "\nEnter type of Encryption : \n1. Caesar Cipher \n2. Vegenary Caesar Cipher\n3. Brute Force Caesar Cipher \n4. Decrypt Caesar Cipher\n5. Decrypt Vegenary Caesar Cipher\n6. Exit");
            choice = sc.nextInt();
            if (choice == 1) {
                System.out.println("Enter plain Text: ");
                String plainText = sc.next();
                System.out.println("Enter key: ");
                int key = sc.nextInt();
                key %= 26;
                String cipherText = basicCaesarCipher(plainText, key);
                System.out.println("Cipher Text : " + cipherText);
            }

            else if (choice == 2) {
                System.out.println("Enter plain Text: ");
                String plainText = sc.next();
                System.out.println("Enter length of key set: ");
                int n = sc.nextInt();
                int[] key = new int[n];
                System.out.println("Enter keys: ");
                for (int i = 0; i < n; i++) {
                    key[i] = sc.nextInt();
                    key[i] %= 26;
                }
                String cipherText = vegenaryCaesarCipher(key, plainText);
                System.out.println("Cipher Text : " + cipherText);
            }

            else if (choice == 3) {
                System.out.println("Enter cipher Text : ");
                String cipherText = sc.next();
                System.out.println("Possible words can be: ");
                bruteForce(cipherText);
            }

            else if (choice == 4) {
                System.out.println("Enter Cipher Text: ");
                String cipherText = sc.next();
                System.out.println("Enter key: ");
                int key = sc.nextInt();
                key %= 26;
                String decryptText = decryptCaesarCipher(cipherText, key);
                System.out.println("Decrypted message : " + decryptText);
            }

            else if (choice == 5) {
                System.out.println("Enter Cipher Text : ");
                String cipherText = sc.next();
                System.out.println("Enter length of key set: ");
                int n = sc.nextInt();
                int[] key = new int[n];
                System.out.println("Enter keys: ");
                for (int i = 0; i < n; i++) {
                    key[i] = sc.nextInt();
                    key[i] %= 26;
                }
                String message = decryptVegenary(cipherText, key);
                System.out.println("Decrypted Message : " + message);
            } else if (choice == 6)
                break;
            else
                System.out.println("Please choose among given options");
        }

        sc.close();
    }

    static String basicCaesarCipher(String plainText, int key) {
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i++) {
            char x = plainText.charAt(i);
            if (Character.isLowerCase(x))
                x = (char) (((x - 'a' + key) % 26) + 97);
            else
                x = (char) (((x - 'A' + key) % 26) + 65);
            cipherText += x;
        }
        return cipherText;
    }

    static String vegenaryCaesarCipher(int[] key, String plainText) {
        String cipherText = "";
        int index = 0;
        for (int i = 0; i < plainText.length(); i++) {
            char x = plainText.charAt(i);
            if (Character.isLowerCase(x))
                x = (char) (((x - 'a' + key[index]) % 26) + 97);
            else
                x = (char) (((x - 'A' + key[index]) % 26) + 65);
            cipherText += x;
            index++;
            if (index >= key.length)
                index = 0;
        }
        return cipherText;

    }

    static String decryptCaesarCipher(String cipherText, int key) {
        String decrypt = "";
        for (int i = 0; i < cipherText.length(); i++) {
            char x = cipherText.charAt(i);
            if (Character.isLowerCase(x))
                x = (char) (((x - 'a' - key) % 26) + 97);
            else
                x = (char) (((x - 'A' - key) % 26) + 65);
            decrypt += x;
        }
        return decrypt;
    }

    static String decryptVegenary(String cipherText, int[] key) {
        String decrypt = "";
        int index = 0;
        System.out.println(cipherText.length());
        for (int i = 0; i < cipherText.length(); i++) {
            char x = cipherText.charAt(i);
            if (Character.isLowerCase(x))
                x = (char) (((x - 'a' - key[index]) % 26) + 97);
            else
                x = (char) (((x - 'A' - key[index]) % 26) + 65);
            decrypt += x;
            index++;
            if (index >= key.length)
                index = 0;
        }
        return decrypt;
    }

    static void bruteForce(String cipherText) {
        int key = 1;
        String decrypt;
        key = 1;
        while (key <= 25) {
            decrypt = "";
            for (int i = 0; i < cipherText.length(); i++) {
                char x = cipherText.charAt(i);
                if (Character.isLowerCase(x))
                    x = (char) (((x - 'a' + key) % 26) + 97);
                else
                    x = (char) (((x - 'A' + key) % 26) + 65);
                decrypt += x;
            }
            System.out.println(decrypt);
            key++;

        }
    }
}