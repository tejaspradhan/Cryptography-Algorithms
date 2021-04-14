import java.util.*;

public class RSA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Choice\n1.Encryption\n2.Decryption");
        int c = sc.nextInt();
        if (c == 1) {
            encrpyt();
        }
        sc.close();
    }

    public static void encrpyt() {
        Scanner sc = new Scanner(System.in);
        int p, q;
        System.out.println("Enter message");
        int M = sc.nextInt();
        System.out.println("Enter two co-prime numbers p & q ");
        p = sc.nextInt();
        q = sc.nextInt();
        int n = p * q;
        int phiN = (p - 1) * (q - 1);
        // int e = generatePublicKey(n, phiN);
        int e = 7;
        int cipher = exp(M, e, n);
        cipher %= n;
        System.out.println("Encrypted Message " + cipher);
        sc.close();
    }

    public static int gcd(int a, int b) {
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }

    public static int exp(int a, int b, int n) {
        if (b == 0)
            return 1;

        else if (b % 2 == 0) {
            int x = exp(a, b / 2, n);
            return (x * x) % n;
        }

        else {
            int x = exp(a, b / 2, n);
            return (a * ((x * x) % n)) % n;
        }
    }

    public static int generatePublicKey(int n, int phiN) {
        int e = n;
        Random r = new Random();
        while (gcd(e, n) != 1) {
            e = r.nextInt(phiN - 1) + 1; // random number between 1,phiN
        }
        return e;
    }

    public static void decrypt() {
    }
}
