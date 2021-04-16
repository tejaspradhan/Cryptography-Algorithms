import java.io.*;
import java.net.Socket;
import java.util.*;

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

public class Client {
    static int A, B, N;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Parameters A,B and N for elliptic Curve");
        A = sc.nextInt();
        B = sc.nextInt();
        N = sc.nextInt();
        Point G = new Point();
        int a;
        System.out.println("Enter co-ordinates of Generator Point");
        G.x = sc.nextInt();
        G.y = sc.nextInt();
        System.out.println("Enter private value a");
        a = sc.nextInt();
        Point Pa = compute(a, G);
        System.out.println("Result " + Pa.x + " " + Pa.y);
        try {
            System.out.println("Client Program Started");
            Socket soc = new Socket("localhost", 8000);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            soc.close();
        }

        catch (Exception e) {
            System.err.println(e);
        }
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

    static int inverse(int num, int n) {
        if (num < 0)
            num += N;
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
