import java.util.*;

public class ECC {
    static int A = 1, B = 6, N = 11;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a, b;
        Point Pm = new Point();
        Point B = new Point();
        int k = 6;
        System.out.println("Enter message Point");
        Pm.x = sc.nextInt();
        Pm.y = sc.nextInt();
        System.out.println("Enter co-ordinates of the base point");
        B.x = sc.nextInt();
        B.y = sc.nextInt();
        System.out.println("Enter private keys a and b");
        a = sc.nextInt();
        b = sc.nextInt();
        Point kB = compute(k, B);
        Point Pb = compute(b, kB);
        System.out.println("Pb: " + Pb.x + " " + Pb.y);
        Point kPb = compute(k, Pb);
        System.out.println("kPB: " + kPb.x + " " + kPb.y);
        Point result = addPoints(Pm, kPb);
        Point bKb = compute(b, kB);
        bKb.y = (-1) * bKb.y;
        System.out.println(
                "Cipher Text (Pc) : " + "(" + kB.x + "," + kB.y + ")" + " " + "(" + result.x + "," + result.y + ")");

        Point newResult = addPoints(result, bKb);
        System.out.println(newResult.x + " " + newResult.y);
        sc.close();

    }

    public static Point addPoints(Point P1, Point P2) {
        int m; // slope
        Point result = new Point();
        if (P1.x == P2.x && P1.y == P2.y) { // equal points
            m = ((3 * P1.x * P1.x) + A) * inverse(2 * P1.y, N);
            m %= N;
        }

        else {
            m = (P1.y - P2.y) * inverse(P1.x - P2.x, N);
            m %= N;
        }

        result.x = ((m * m) - P1.x - P2.x) % N;
        result.y = (m * (P1.x - result.x) - P1.y) % N;
        while (result.x < 0)
            result.x += N;
        while (result.y < 0)
            result.y += N;
        return result;
    }

    public static int inverse(int num, int mod) {
        if (num < 0)
            num += mod;
        int a = num, m = mod, m0 = m;
        int y = 0, x = 1;

        if (m == 1)
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

    public static Point compute(int n, Point P) {
        // computes the value of nP
        Point result = P;
        n--; // as result initialised to P
        while (n > 0) {
            result = addPoints(result, P);
            n--;
        }
        return result;
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
}

class Point {
    public int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point() {
        this.x = 0;
        this.y = 0;
    }
}
