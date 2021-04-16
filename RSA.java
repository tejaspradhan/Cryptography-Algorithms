import java.util.*;

public class RSA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Choice\n1.Encryption\n2.Decryption");
        int c = sc.nextInt();
        if (c == 1) {
            encrpyt();
        }

        else if (c == 2) {
            decrypt();
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
        System.out.println("Value of e : " + e);
        System.out.println("Public Key Pair : " + e + " " + n);
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
        Scanner sc = new Scanner(System.in);
        int e, C, n;
        System.out.println("Enter cipher");
        C = sc.nextInt();
        System.out.println("Enter public key pair");
        e = sc.nextInt();
        n = sc.nextInt();
        int d = inverse(e, n);
        int M = exp(C, e, n);
        System.out.println("Decrpyted message : " + M);
        sc.close();
    }

    static int inverse(int num, int n) {
        if (num < 0)
            num += n;
        int a = num, m = n, m0 = m;
        int y = 0, x = 1;

        if (m == 1) // base case
            return 0;

        while (a > 1) {
            int q = a / m;
            int t = m;
            m = a % m;
            a = t;
            t = y;
            y = x - q * y;
            x = t;
        }
        if (x < 0)
            x += m0;
        return x;
    }
}
